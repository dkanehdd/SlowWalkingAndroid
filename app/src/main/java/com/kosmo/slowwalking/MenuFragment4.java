package com.kosmo.slowwalking;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MenuFragment4 extends Fragment implements CompoundButton.OnCheckedChangeListener {

    public static final String TAG = "ikosmo";
    String[] parents = {"받은후기","이용권","","CCTV"};
    String[] sitter = {"받은후기","이용권","", "CCTV"};
    String id, flag;
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "MenuFragement4 > onCreateView()");

        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.menu_fragment4, container, false);
        listView = (ListView)viewGroup.findViewById(android.R.id.list);

        Bundle bundle = getArguments();
        id = bundle.getString("id");
        flag = bundle.getString("flag");
        Switch switch_btn = (Switch)viewGroup.findViewById(R.id.switchbutton);
        if(flag.equals("parents")){
            switch_btn.setVisibility(View.GONE);
        }
        else{
            parents= sitter;
        }
        String addr = getResources().getString(R.string.server_addr);
        new MemberView().execute(
                addr+"myinfo",
                "id="+id
        );
        switch_btn.setOnCheckedChangeListener(this);


        return viewGroup;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String addr = getResources().getString(R.string.server_addr);
        new advertiseChange().execute( //1. 수락안된 인터뷰리스트 불러오기
                addr+"advertise",
                "id="+id,
                "check="+isChecked
        );
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if(flag.equals("sitter")){
                return sitter.length;
            }
            return parents.length;
        }

        @Override
        public Object getItem(int position) {
            if(flag.equals("sitter")){
                return sitter[position];
            }
            return parents[position];
        }

        @Override
        public long getItemId(int position) {
            if(flag.equals("sitter")){
                return position;
            }
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            IdolView idolView = new IdolView(getContext());
            if(flag.equals("sitter")){
                idolView.setName(sitter[position]);
            }
            idolView.setName(parents[position]);

            return idolView;

        }
    }

    class advertiseChange extends AsyncTask<String, Void, String> {

        public static final String TAG = "ikosmo";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            StringBuffer receiveData = new StringBuffer();
            try {
                URL url = new URL(strings[0]);//파라미터1 : 요청 URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                out.write(strings[1].getBytes());
                out.write("&".getBytes());
                out.write(strings[2].getBytes());

                out.flush();
                out.close();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.i(TAG, "HTTP OK 성공");

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    String responseData;
                    while ((responseData = reader.readLine()) != null) {
                        receiveData.append(responseData + "\n\r");
                    }
                    reader.close();
                } else {
                    Log.i(TAG, connection.getResponseCode() + "");
                    Log.i(TAG, "HTTP OK 안됨");
                }
            } catch (Exception e) {
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
            try {
                //JSON객체를 파싱
                JSONObject jsonObject = new JSONObject(s);
                String message = jsonObject.get("message").toString();

                Toast.makeText(getContext(),
                        message,
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //시터리스트 불러오기
    class MemberView extends AsyncTask<String, Void, String> {


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
                JSONObject dto = (JSONObject)jsonObject.get("dto");//lists로 배열을 먼저 얻어옴 []
                String point = dto.getString("point");
                String ticket = dto.getString("ticket");
                sitter[1] = "이용권 " + ticket+ "개";
                parents[1] = "이용권 " + ticket+ "개";
                sitter[2] = point+"포인트";
                parents[2] = point+"포인트";
                MenuFragment4.MyAdapter myAdapter = new MenuFragment4.MyAdapter();
                listView.setAdapter(myAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {
                        Log.d(TAG, "인덱스 : "+position);
                        if(position==0){
                            Intent intent = new Intent(getContext(),
                                    CommentActivity.class);
                            //부가데이터를 넘기기 위한 준비. Map컬렉션같이 Key와 value로 설정
                            intent.putExtra("id", id);
                            //액티비티 실행
                            startActivity(intent);
                        }
                        if(position==3) {
                            Intent intent = new Intent(getContext(),
                                    CctvActivity.class);
                            //부가데이터를 넘기기 위한 준비. Map컬렉션같이 Key와 value로 설정
                            //액티비티 실행
                            startActivity(intent);
                        }
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    
}

