package com.kosmo.slowwalking;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MenuFragment1 extends Fragment {

    public static final String TAG = "iKosmo";

    SubMenuFragment1 submenuFragment1;
    SubMenuFragment2 submenuFragment2;


    ArrayList<String> sitter_id = new ArrayList<>();
    ArrayList<String> image_view = new ArrayList<String>() ;
    ArrayList<String> requestname = new ArrayList<String>();
    ArrayList<String> requestaddress = new ArrayList<String>();
    ArrayList<Integer> requestage = new ArrayList<>();
    ArrayList<Integer> requestaccount = new ArrayList<>();
    ArrayList<Integer> requeststarrate = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "MenuFragement1 > onCreateView()");

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.menu_fragment1, container, false);
       // ListView listView = (ListView) viewGroup.findViewById(R.id.listview1);

        Button button1 = (Button)viewGroup.findViewById(R.id.btnsubFirstFragment);
        Button button2 = (Button)viewGroup.findViewById(R.id.btnsubSecondFragment);
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);

        FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();
        submenuFragment1 = new SubMenuFragment1();
        submenuFragment2 = new SubMenuFragment2();

        mFragmentTransaction.replace(R.id.mainLayout, submenuFragment1).commit();

        new SitterList().execute( //수락된 인터뷰 리스트 불러오기
                "http://192.168.219.107:8080/slowwalking/android/SitterBoard_list"

        );

        submenuFragment1 = new SubMenuFragment1();
        Bundle bundle2 = new Bundle();
        bundle2.putStringArrayList("sitter_id", sitter_id);
        bundle2.putStringArrayList("image_view", image_view);//번들객체에 리스트로 담아서
        bundle2.putStringArrayList("requestname", requestname);
        bundle2.putStringArrayList("requestaddress", requestaddress);
        bundle2.putIntegerArrayList("requestage", requestage);
        bundle2.putIntegerArrayList("requestaccount", requestaccount);
        bundle2.putIntegerArrayList("requeststarrate", requeststarrate);
        submenuFragment1.setArguments(bundle2);//프래그먼트에 세팅


        return viewGroup;

    }

    View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.btnsubFirstFragment){
                getChildFragmentManager().beginTransaction().replace(R.id.mainLayout, submenuFragment1).commit();
            }
            else if(view.getId()==R.id.btnsubSecondFragment){
                getChildFragmentManager().beginTransaction().replace(R.id.mainLayout, submenuFragment2).commit();
            }
        }
    };

    //인터뷰 리스트 불러오기
    class SitterList extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            StringBuffer receiveData = new StringBuffer();
            try{
                URL url = new URL(strings[0]);//파라미터1 : 요청 URL
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                out.flush();
                out.close();

                if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    Log.i(TAG, "HTTP OK 성공");

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(),"UTF-8"));
                    String responseData;
                    while((responseData=reader.readLine())!=null){
                        receiveData.append(responseData+"\n\r");
                    }
                    reader.close();
                }
                else{
                    Log.i(TAG, connection.getResponseCode()+"");
                    Log.i(TAG, "HTTP OK 안됨");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            //저장된 내용을 로그로 출력한후 onPostExecute()로 반환한다.
            Log.i(TAG, receiveData.toString());
            //서버에서 내려준 JSON정보를 저장후 반환
            return receiveData.toString();
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            StringBuffer sb = new StringBuffer();
            try{
                /*
                {"lists":[{"idx":8,"parents_id":"dkanehdd","sitter_id":"dkanehd","request_time":"15:00 ~ 20:00",
                "parents_agree":"T","sitter_agree":"T","request_idx":10,"parents_name":null,"sitter_name":null},
                {"idx":7,"parents_id":"kosmo3","sitter_id":"dkanehd","request_time":"18:00 ~ 21:00","parents_agree":"T",
                "sitter_agree":"T","request_idx":9,"parents_name":null,"sitter_name":null}]}
                 */
                //JSON객체를 파싱
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = (JSONArray)jsonObject.get("lists");//lists로 배열을 먼저 얻어옴 []

                for(int i=0 ; i<jsonArray.length() ; i++){//배열크기만큼반복
                    JSONObject interview = (JSONObject) jsonArray.get(i); //배열에서 하나씩 가져옴
                    requestname.add(interview.get("name").toString());//가져와서 컬렉션에 저장
                    requestaddress.add(interview.get("residence1").toString());
                    image_view.add(interview.get("image_path").toString());
                    sitter_id.add(interview.get("sitter_id").toString());
                    requestage.add(Integer.parseInt(interview.get("age").toString()));
                    requestaccount.add(Integer.parseInt(interview.get("pay").toString()));
                    requeststarrate.add(Integer.parseInt(interview.get("starrate").toString()));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
