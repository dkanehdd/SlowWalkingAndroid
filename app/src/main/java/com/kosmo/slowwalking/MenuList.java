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
import android.widget.LinearLayout;

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
    private View header;
    calendarFragment calendarFragment;
    SubMenuFragment1 subMenuFragment1;

    ArrayList<String> interviewID = new ArrayList<String>() ;
    ArrayList<String> interviewRequest_time = new ArrayList<String>() ;
    ArrayList<Integer> interview_idx = new ArrayList<Integer>();
    ArrayList<Integer> diary_idx = new ArrayList<Integer>();
    ArrayList<String> Request_time = new ArrayList<String>() ;
    ArrayList<String> parentsName = new ArrayList<String>();

    ArrayList<String> regidate = new ArrayList<String>() ;

    ArrayList<String> name = new ArrayList<String>() ;
    ArrayList<String> content = new ArrayList<String>() ;


    //시터리스트
    ArrayList<String> sitter_id = new ArrayList<>();
    ArrayList<String> image_view = new ArrayList<String>() ;
    ArrayList<String> requestname = new ArrayList<String>();
    ArrayList<String> requestaddress = new ArrayList<String>();
    ArrayList<Integer> requestage = new ArrayList<>();
    ArrayList<Integer> requestaccount = new ArrayList<>();
    ArrayList<Integer> requeststarrate = new ArrayList<>();

    //시터리스트 상세
    ArrayList<String> requestintro = new ArrayList<String>();
    ArrayList<String> requestcctv = new ArrayList<String>();
    ArrayList<String> requestpersnal = new ArrayList<String>();
    ArrayList<String> requestlicense = new ArrayList<String>();
    ArrayList<String> requestactivity = new ArrayList<String>();
    ArrayList<String> requestdate = new ArrayList<String>();
    ArrayList<String> requestaddress2 = new ArrayList<String>();
    ArrayList<String> requestaddress3 = new ArrayList<String>();
    ArrayList<Integer> requestpay = new ArrayList<>();
    ArrayList<String> requestgender = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        final Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        String id = intent.getStringExtra("id");
        if(flag.equals("sitter")){

        }
        new InterviewAsyncHttpRequest().execute( //1. 수락안된 인터뷰리스트 불러오기
                "http://192.168.219.119:8080/slowwalking/android/interList",
                "id="+id,
                "flag="+flag
        );
        new DiaryAsyncHttpRequest().execute( //수락된 인터뷰 리스트 불러오기
                "http://192.168.219.119:8080/slowwalking/android/diaryList",
                "id="+id,
                "flag="+flag
        );
        new DiaryListAsync().execute( //수락된 인터뷰 리스트 불러오기
                "http://192.168.219.119:8080/slowwalking/android/openCalendar",
                "id="+id,
                "flag="+flag
        );
        new SitterList().execute( //시터 리스트 불러오기
                "http://192.168.219.119:8080/slowwalking/android/SitterBoard_list"

        );

        new SitterDetail().execute( //시터 리스트 불러오기
                "http://192.168.219.119:8080/slowwalking/android/SitterBoard_list"

        );


        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        menuFragment1 = new MenuFragment1();

        
        menuFragment2 = new MenuFragment2();
        Bundle bundle2 = new Bundle();
        bundle2.putStringArrayList("interviewID", interviewID);//번들객체에 리스트로 담아서
        bundle2.putStringArrayList("request_time", interviewRequest_time);
        bundle2.putIntegerArrayList("interview_idx", interview_idx);
        bundle2.putString("flag", flag);
        menuFragment2.setArguments(bundle2);//프래그먼트에 세팅

        menuFragment3 = new MenuFragment3();
        Bundle bundle3 = new Bundle();
        bundle3.putStringArrayList("parentsName", parentsName);
        bundle3.putStringArrayList("request_time", Request_time);
        bundle3.putIntegerArrayList("diary_idx", diary_idx);
        menuFragment3.setArguments(bundle3);


        menuFragment4 = new MenuFragment4();
        Bundle bundle6 = new Bundle();
        bundle6.putString("id", id);
        menuFragment4.setArguments(bundle6);


        calendarFragment = new calendarFragment();
        Bundle bundle5 = new Bundle();
        bundle5.putStringArrayList("regidate", regidate);
        bundle5.putStringArrayList("content", content);
        bundle5.putStringArrayList("name", name);
        calendarFragment.setArguments(bundle5);

        /*subMenuFragment1 = new SubMenuFragment1();*/
        Bundle bundle4 = new Bundle();
        bundle4.putStringArrayList("sitter_id", sitter_id);
        bundle4.putStringArrayList("image_path", image_view);//번들객체에 리스트로 담아서
        bundle4.putStringArrayList("name", requestname);
        bundle4.putStringArrayList("introduction", requestintro);
        bundle4.putStringArrayList("cctv_agree", requestcctv);
        bundle4.putStringArrayList("personality_check", requestpersnal);
        bundle4.putStringArrayList("license_check", requestlicense);
        bundle4.putStringArrayList("activity_time", requestactivity);
        bundle4.putStringArrayList("activity_date", requestdate);
        bundle4.putStringArrayList("residence1", requestaddress);
        bundle4.putStringArrayList("residence2", requestaddress2);
        bundle4.putStringArrayList("residence3", requestaddress3);
        bundle4.putIntegerArrayList("age", requestage);
        bundle4.putIntegerArrayList("pay", requestaccount);
        bundle4.putStringArrayList("gender", requestgender);
        bundle4.putIntegerArrayList("starrate", requeststarrate);
        menuFragment1.setArguments(bundle4);//프래그먼트에 세팅
        /*subMenuFragment1 = new SubMenuFragment1();
        subMenuFragment1.setArguments(bundle4);
        header=  getLayoutInflater().inflate(R.layout.menu_fragment1, null, false);
        LinearLayout sub1 = (LinearLayout)header.findViewById(R.id.mainLayout);*/
       fragmentTransaction.replace(R.id.frameLayout, menuFragment1).commit();//첨 화면진입시 1번이 보여짐*/
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
                    if(flag.equals("sitter")){
                        transaction.replace(R.id.frameLayout, menuFragment3).commitAllowingStateLoss();
                    }
                    else{
                        transaction.replace(R.id.frameLayout, calendarFragment).commitAllowingStateLoss();
                    }
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
                    if(flag.equals("sitter")){
                        interviewID.add(interview.get("parents_name").toString());//가져와서 컬렉션에 저장
                    }
                    else{
                        interviewID.add(interview.get("sitter_name").toString());
                    }
                    interviewRequest_time.add(interview.get("request_time").toString());//가져와서 컬렉션에 저장
                    interview_idx.add(Integer.parseInt(interview.get("idx").toString()));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    //인터뷰 리스트 불러오기
    class DiaryAsyncHttpRequest extends AsyncTask<String, Void, String> {


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

                 */
                //JSON객체를 파싱
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = (JSONArray)jsonObject.get("lists");//lists로 배열을 먼저 얻어옴 []

                for(int i=0 ; i<jsonArray.length() ; i++){//배열크기만큼반복
                    JSONObject interview = (JSONObject) jsonArray.get(i); //배열에서 하나씩 가져옴
                    Log.i(TAG, interview.get("parents_id").toString()+" "+interview.get("request_time").toString());//디버깅용
                    if(flag.equals("sitter")){
                        parentsName.add(interview.get("parents_name").toString());//가져와서 컬렉션에 저장
                    }
                    else{
                        interviewID.add(interview.get("sitter_name").toString());
                    }
                    Request_time.add(interview.get("request_time").toString());
                    diary_idx.add(Integer.parseInt(interview.get("idx").toString()));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class DiaryListAsync extends AsyncTask<String, Void, String> {


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

                 */
                //JSON객체를 파싱
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = (JSONArray)jsonObject.get("lists");//lists로 배열을 먼저 얻어옴 []

                for(int i=0 ; i<jsonArray.length() ; i++){//배열크기만큼반복
                    JSONObject diary = (JSONObject) jsonArray.get(i); //배열에서 하나씩 가져옴
                    regidate.add(diary.get("regidate").toString());
                    content.add(diary.get("content").toString());
                    name.add(diary.get("name").toString());
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //시터리스트 불러오기
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
                    JSONObject sitterview = (JSONObject) jsonArray.get(i); //배열에서 하나씩 가져옴
                    requestname.add(sitterview.get("name").toString());//가져와서 컬렉션에 저장
                    requestintro.add(sitterview.get("name").toString());//가져와서 컬렉션에 저장
                    requestname.add(sitterview.get("name").toString());//가져와서 컬렉션에 저장
                    requestname.add(sitterview.get("name").toString());//가져와서 컬렉션에 저장
                    requestname.add(sitterview.get("name").toString());//가져와서 컬렉션에 저장
                    requestname.add(sitterview.get("name").toString());//가져와서 컬렉션에 저장
                    requestaddress.add(sitterview.get("residence1").toString());
                    image_view.add(sitterview.get("image_path").toString());
                    sitter_id.add(sitterview.get("sitter_id").toString());
                    requestage.add(Integer.parseInt(sitterview.get("age").toString()));
                    requestaccount.add(Integer.parseInt(sitterview.get("pay").toString()));
                    requeststarrate.add(Integer.parseInt(sitterview.get("starrate").toString()));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //상세시터리스트 불러오기
    class SitterDetail extends AsyncTask<String, Void, String> {


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
                    JSONObject sitterdetailview = (JSONObject) jsonArray.get(i); //배열에서 하나씩 가져옴
                    requestname.add(sitterdetailview.get("name").toString());//가져와서 컬렉션에 저장
                    requestintro.add(sitterdetailview.get("introduction").toString());
                    requestcctv.add(sitterdetailview.get("cctv_agree").toString());
                    requestpersnal.add(sitterdetailview.get("personality_check").toString());
                    requestlicense.add(sitterdetailview.get("license_check").toString());
                    requestactivity.add(sitterdetailview.get("activity_time").toString());
                    requestdate.add(sitterdetailview.get("activity_date").toString());
                    requestaddress1.add(sitterdetailview.get("residence1").toString());
                    requestaddress2.add(sitterdetailview.get("residence2").toString());
                    requestaddress3.add(sitterdetailview.get("residence3").toString());
                    requestgender.add(sitterdetailview.get("gender").toString());
                    requestage.add(Integer.parseInt(sitterdetailview.get("age").toString()));
                    requestpay.add(Integer.parseInt(sitterdetailview.get("pay").toString()));
                    requeststarrate.add(Integer.parseInt(sitterdetailview.get("starrate").toString()));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}