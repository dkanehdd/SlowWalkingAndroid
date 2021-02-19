package com.kosmo.slowwalking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class InterView extends LinearLayout {
    TextView textView1;
    TextView textView2;
    Button btnagree;
    Button btndelete;
    LinearLayout interviewItem;
    //생성자
    public InterView(Context context) {
        super(context);
        //레이아웃의 전개를 위해 Inflater객체를 생성함
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
        //inflate()를 통해 레이아웃을 전개한다
        inflater.inflate(R.layout.interview_view, this, true);
        //데이터를 출력할 위젯을 얻어온다.
        interviewItem = findViewById(R.id.interviewItems);
        textView1 = findViewById(R.id.parent_id);
        textView2 = findViewById(R.id.request_time);
        btnagree = findViewById(R.id.Btnagree);
        btndelete = findViewById(R.id.Btndelete);
    }

    //각 항목을 설정할 setter() 정의
    public void setName(String name) {
        textView1.setText(name);
    }

    public void setPhone(String Phone) {
        textView2.setText(Phone);
    }

    public void setAgree(int idx, String flag) {
        btnagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addr = getResources().getString(R.string.server_addr);
                new InterviewAgree().execute(
                        addr+"agreeAction",
                        "idx=" + idx,
                        "flag=" + flag
                );
            }
        });
    }

    public void setDelete(int idx, String flag) {
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(idx, flag);
            }
        });
    }


    class InterviewAgree extends AsyncTask<String, Void, String> {

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
                String mode = jsonObject.get("mode").toString();
                Toast.makeText(getContext(),
                        message,
                        Toast.LENGTH_SHORT).show();
                if (mode.equals("agree")) {
                    btnagree.setText("대기");
                } else {
                    interviewItem.setVisibility(interviewItem.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void showDialog(int idx, String flag) {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(getContext())
                .setTitle("삭제")
                .setMessage("정말 삭제하시겠습니까?")
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new InterviewAgree().execute(
                        "http://192.168.219.107:8080/slowwalking/android/deleteAction",
                        "idx=" + idx,
                        "flag=" + flag
                );
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }
}
