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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sitter_detailview);

        final Intent intent = getIntent();
        String Sitter_id = intent.getStringExtra("sitter_id");
        String image_view = intent.getStringExtra("image_path");
        String requestname = intent.getStringExtra("name");
        String requestintro = intent.getStringExtra("introduction");
        String requestcctv = intent.getStringExtra("cctv_agree");
        String requestpersnal = intent.getStringExtra("personality_check");
        String requestlicense = intent.getStringExtra("license_check");
        String requestactivity = intent.getStringExtra("activity_time");
        String requestdate = intent.getStringExtra("activity_date");
        String requestaddress1 = intent.getStringExtra("residence1");
        String requestaddress2 = intent.getStringExtra("residence2");
        String requestaddress3 = intent.getStringExtra("residence3");
        int requestage = intent.getIntExtra("age", 0);
        int requestpay = intent.getIntExtra("pay", 0);
        String requestgender = intent.getStringExtra("gender");
        int requeststarrate = intent.getIntExtra("starrate", 0);

        ImageView imageview = (ImageView) findViewById(R.id.imageview);
        TextView sittername = (TextView) findViewById(R.id.request_name);
        TextView request_intro = (TextView) findViewById(R.id.request_intro);
        TextView request_cctv = (TextView) findViewById(R.id.request_cctv);
        TextView request_personal = (TextView) findViewById(R.id.request_personal);
        TextView prequest_licenserent = (TextView) findViewById(R.id.request_license);
        TextView request_time = (TextView) findViewById(R.id.request_activity);
        TextView request_date = (TextView) findViewById(R.id.request_date);
        TextView request_address1 = (TextView) findViewById(R.id.request_address1);
        TextView request_address2 = (TextView) findViewById(R.id.request_address2);
        TextView request_address3 = (TextView) findViewById(R.id.request_address3);
        TextView request_age = (TextView) findViewById(R.id.request_age);
        TextView request_pay = (TextView) findViewById(R.id.request_pay);
        TextView request_gender = (TextView) findViewById(R.id.request_gender);
        RatingBar rating = (RatingBar) findViewById(R.id.request_starrate);

        imageview.setImageDrawable(Drawable.createFromPath(image_view));
        sittername.setText(requestname);
        request_intro.setText(requestintro);
        request_cctv.setText(requestcctv);
        request_personal.setText(requestpersnal);
        prequest_licenserent.setText(requestlicense);
        request_time.setText(requestactivity);
        request_date.setText(requestdate);
        request_address1.setText(requestaddress1);
        request_address2.setText(requestaddress2);
        request_address3.setText(requestaddress1);
        request_age.setText(Integer.toString(requestage));
        request_pay.setText(Integer.toString(requestpay));
        request_gender.setText(requestgender);
        rating.setRating(requeststarrate);

    }

    class SitterDetail extends AsyncTask<String, Void, String> {


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
                connection.setRequestMethod("POST");
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
    }
}


