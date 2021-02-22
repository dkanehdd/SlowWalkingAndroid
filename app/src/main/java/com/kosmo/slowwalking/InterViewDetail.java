package com.kosmo.slowwalking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InterViewDetail extends AppCompatActivity {

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
    TextView request_address2;
    TextView request_address3;
    TextView request_age;
    TextView request_pay;
    TextView request_gender;
    RatingBar rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sitter_detailview);

        final Intent intent = getIntent();
        String Sitter_id = intent.getStringExtra("sitter_id");
        String addr = getResources().getString(R.string.server_addr);
        new SitterDetail().execute( //시터 리스트 불러오기
                addr+"SitterBoard_view",
                "id="+Sitter_id
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
        request_address2 = (TextView) findViewById(R.id.request_address2);
        request_address3 = (TextView) findViewById(R.id.request_address3);
        request_age = (TextView) findViewById(R.id.request_age);
        request_pay = (TextView) findViewById(R.id.request_pay);
        request_gender = (TextView) findViewById(R.id.request_gender);
        rating = (RatingBar) findViewById(R.id.request_starrate);



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

                sittername.setText(sitterdetailview.get("name").toString());
                request_intro.setText(sitterdetailview.get("introduction").toString());
                request_cctv.setText(sitterdetailview.get("cctv_agree").toString());
                request_personal.setText(sitterdetailview.get("personality_check").toString());
                prequest_licenserent.setText(sitterdetailview.get("license_check").toString());
                request_time.setText(sitterdetailview.get("activity_time").toString());
                request_date.setText(sitterdetailview.get("activity_date").toString());
                request_address1.setText(sitterdetailview.get("residence1").toString());
                request_address2.setText(sitterdetailview.get("residence2").toString());
                request_address3.setText(sitterdetailview.get("residence3").toString());
                request_age.setText(sitterdetailview.get("age").toString());
                request_pay.setText(sitterdetailview.get("pay").toString());
                request_gender.setText(sitterdetailview.get("gender").toString());
                rating.setRating(Integer.parseInt(sitterdetailview.get("starrate").toString()));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}


