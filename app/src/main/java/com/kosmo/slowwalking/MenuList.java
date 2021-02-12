package com.kosmo.slowwalking;

import android.content.ClipData;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MenuList extends AppCompatActivity {

    public static final String TAG = "iKosmo";

    String flag;
    FragmentManager fragmentManager;
    MenuFragment1 menuFragment1;
    MenuFragment2 menuFragment2;
    MenuFragment3 menuFragment3;
    MenuFragment4 menuFragment4;
    MenuFragment5 menuFragment5;

    ArrayList<String> interviewID = new ArrayList<String>() ;
    ArrayList<String> request_time = new ArrayList<String>() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        final Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        String id = intent.getStringExtra("id");
        if(flag.equals("sitter")){

        }
        new InterviewAsyncHttpRequest().execute( //1. 인터뷰리스트 불러오기
                "http://192.168.35.123:8080/slowwalking/android/interList",
                "id="+id,
                "flag="+flag
        );

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        menuFragment1 = new MenuFragment1();
        
        
        menuFragment2 = new MenuFragment2();
        Bundle bundle2 = new Bundle();
        bundle2.putStringArrayList("interviewID", interviewID);//번들객체에 리스트로 담아서
        bundle2.putStringArrayList("request_time", request_time);
        menuFragment2.setArguments(bundle2);//프래그먼트에 세팅

        menuFragment3 = new MenuFragment3();
        menuFragment4 = new MenuFragment4();
        menuFragment5 = new MenuFragment5();

        fragmentTransaction.replace(R.id.frameLayout, menuFragment1).commit();//첨 화면진입시 1번이 보여짐

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());//하단바에 리스너 부착
    }
    //리스너 설정
    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.homeItem:
                    transaction.replace(R.id.frameLayout, menuFragment1).commitAllowingStateLoss();
                    break;
                case R.id.interviewItem:
                    transaction.replace(R.id.frameLayout, menuFragment2).commitAllowingStateLoss();
                    break;
                case R.id.dialyItem:
                    transaction.replace(R.id.frameLayout, menuFragment3).commitAllowingStateLoss();
                    break;
                case R.id.mypageItem:
                    transaction.replace(R.id.frameLayout, menuFragment4).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

    //인터뷰 리스트 불러오기
    class InterviewAsyncHttpRequest extends AsyncTask<String, Void, String> {


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
                out.write(strings[1].getBytes());
                out.write("&".getBytes());
                out.write(strings[2].getBytes());

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

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        //doInBackground()에서 반환값은 해당 메소드로 전달한다.
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
                    Log.i(TAG, interview.get("parents_id").toString()+" "+interview.get("request_time").toString());//디버깅용
                    interviewID.add(interview.get("parents_id").toString());//가져와서 컬렉션에 저장
                    request_time.add(interview.get("request_time").toString());//가져와서 컬렉션에 저장
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}