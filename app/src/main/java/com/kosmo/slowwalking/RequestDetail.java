package com.kosmo.slowwalking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestDetail extends AppCompatActivity {


    public static final String TAG = "iKosmo";

    ImageView imageview;
    TextView request_title;
    TextView request_childrenname;
    TextView request_date;
    TextView request_ages;
    TextView request_names;
    TextView region;
    TextView request_disability_grade;
    TextView request_warning;
    TextView request_start_work;
    TextView request_regular_short;
    RatingBar rating;
    Button interView;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parentsdetailview);

        final Intent intent = getIntent();
        id = intent.getStringExtra("id");
        String parents_id = intent.getStringExtra("parents_id");
        int idx = intent.getIntExtra("idx",0);
        String flag = intent.getStringExtra("flag");
        String addr = getResources().getString(R.string.server_addr);


        imageview = (ImageView) findViewById(R.id.imageviews);
        request_title = (TextView) findViewById(R.id.request_title);
        request_childrenname = (TextView) findViewById(R.id.request_childrenname);
        request_ages = (TextView) findViewById(R.id.request_ages);
        request_names = (TextView) findViewById(R.id.request_names);
        region = (TextView) findViewById(R.id.region);
        request_date = (TextView)findViewById(R.id.request_date) ;
        request_disability_grade = (TextView) findViewById(R.id.request_disability_grade);
        request_warning = (TextView) findViewById(R.id.request_warning);
        request_start_work = (TextView) findViewById(R.id.request_start_work);
        request_regular_short = (TextView) findViewById(R.id.request_regular_short);
        rating = (RatingBar) findViewById(R.id.request_starrates);

        interView = (Button) findViewById(R.id.BtnreinterView);
        if(flag.equals("parents")) {
            interView.setVisibility(View.GONE);
        }
        new Requestdetail().execute( //시터 리스트 불러오기
                addr+"requestBoard_view",
                "idx="+idx
        );

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
                                        addr+"addRequestList",
                                        "id=" + id,
                                        "activity_time="+ request_date.getText(),
                                        "parents_id="+parents_id,
                                        "idx="+idx
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
                out.write("&".getBytes());
                out.write(strings[4].getBytes());

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
    class Requestdetail extends AsyncTask<String, Void, String> {

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
                JSONObject requestdetailview = (JSONObject)jsonObject.get("dto");//lists로 배열을 먼저 얻어옴 []

                request_title.setText(requestdetailview.get("title").toString());
                request_childrenname.setText(requestdetailview.get("children_name").toString());
                request_ages.setText(requestdetailview.get("age").toString());
                request_names.setText(requestdetailview.get("name").toString());
                request_date.setText(requestdetailview.get("request_date").toString()+requestdetailview.get("request_time").toString());
                region.setText(requestdetailview.get("region").toString());
                request_disability_grade.setText(requestdetailview.get("disability_grade").toString());
                request_warning.setText(requestdetailview.get("warning").toString());
                request_start_work.setText(requestdetailview.get("start_work").toString());
                request_regular_short.setText(requestdetailview.get("regular_short").toString());
                rating.setRating(Integer.parseInt(requestdetailview.get("starrate").toString()));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
