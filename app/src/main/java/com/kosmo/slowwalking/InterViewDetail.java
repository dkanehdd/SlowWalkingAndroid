package com.kosmo.slowwalking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InterViewDetail extends AppCompatActivity implements Runnable{

    public static final String TAG = "iKosmo";


    ImageView imageview;
    TextView sittername;
    TextView request_intro;
    TextView request_cctv;
    TextView request_personal;
    TextView prequest_licenserent;
    TextView request_time;
    TextView request_date;
    TextView request_address1;
    TextView request_pay;
    RatingBar rating;
    Button interView;
    String image;
    Bitmap bitmap;// 비트맵 객체
    // 메인 스레드와 백그라운드 스레드 간의 통신
    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 서버에서 받아온 이미지를 핸들러를 경유해 이미지뷰에 비트맵 리소스 연결
            imageview.setImageBitmap(bitmap);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sitter_detailview);

        final Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String sitter_id = intent.getStringExtra("sitter_id");
        String flag = intent.getStringExtra("flag");
        String addr = getResources().getString(R.string.server_addr);
        new SitterDetail().execute( //시터 리스트 불러오기
                addr+"SitterBoard_view",
                "id="+sitter_id
        );

        imageview = (ImageView) findViewById(R.id.imageview);
        sittername = (TextView) findViewById(R.id.request_name);
        request_intro = (TextView) findViewById(R.id.request_intro);
        request_cctv = (TextView) findViewById(R.id.request_cctv);
        request_personal = (TextView) findViewById(R.id.request_personal);
        prequest_licenserent = (TextView) findViewById(R.id.request_license);
        request_time = (TextView) findViewById(R.id.request_activity);
        request_date = (TextView) findViewById(R.id.request_date);
        request_address1 = (TextView) findViewById(R.id.request_address1);
        request_pay = (TextView) findViewById(R.id.request_pay);
        rating = (RatingBar) findViewById(R.id.request_starrate);

        interView = (Button) findViewById(R.id.BtninterView);
        if(flag.equals("sitter")) {
            interView.setVisibility(View.GONE);
        }



        interView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("알림");
                builder.setMessage("인터뷰 신청하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                String addr = getString(R.string.server_addr);
                                new InterAction().execute(
                                        addr+"addList",
                                        "id=" + id,
                                        "activity_time="+ request_time.getText(),
                                        "sitterBoard_id="+sitter_id
                                );
                                finish();
                            }
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.show();
            }
        });
    }

    class InterAction extends AsyncTask<String, Void, String> {

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
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                out.write(strings[1].getBytes());
                out.write("&".getBytes());
                out.write(strings[2].getBytes());
                out.write("&".getBytes());
                out.write(strings[3].getBytes());

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
                //JSON객체를 파싱
                JSONObject jsonObject = new JSONObject(s);
                String message = jsonObject.getString("message");
                Toast.makeText(getApplicationContext(),
                        message,
                        Toast.LENGTH_SHORT).show();
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
                JSONObject sitterdetailview = (JSONObject)jsonObject.get("dto");//lists로 배열을 먼저 얻어옴 []

                sittername.setText(sitterdetailview.get("name").toString()+"("+sitterdetailview.get("age").toString()+"세, "+sitterdetailview.get("gender").toString()+")");
                request_cctv.setText(sitterdetailview.get("cctv_agree").toString().equals("true")?"CCTV촬영동의":"CCTV촬영동의안함");
                request_intro.setText(sitterdetailview.get("introduction").toString());
                request_personal.setText( "실명 / 생년월일 / 연락처를 확인하였습니다.");
                prequest_licenserent.setText("장애영유아보육교사 자격증을 확인하였습니다.");
                request_time.setText(sitterdetailview.get("activity_time").toString());
                request_date.setText(sitterdetailview.get("activity_date").toString());
                request_address1.setText(sitterdetailview.get("residence1").toString());
                request_pay.setText(sitterdetailview.get("pay").toString()+"원");
                rating.setRating(Integer.parseInt(sitterdetailview.get("starrate").toString()));
                image = sitterdetailview.get("image_path").toString();
                Thread th =new Thread(InterViewDetail.this);
                // 동작 수행
                th.start();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    // 백그라운드 스레드
    @Override
    public void run() {
        URL url =null;
        try{
            // 스트링 주소를 url 형식으로 변환
            url =new URL("http://192.168.219.130:8080/slowwalking/resources/images/"+image);
            // url에 접속 시도
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.connect();
            // 스트림 생성
            InputStream is = conn.getInputStream();
            // 스트림에서 받은 데이터를 비트맵 변환
            // 인터넷에서 이미지 가져올 때는 Bitmap을 사용해야함
            bitmap = BitmapFactory.decodeStream(is);

            // 핸들러에게 화면 갱신을 요청한다.
            handler.sendEmptyMessage(0);
            // 연결 종료
            is.close();
            conn.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}


