package com.kosmo.slowwalking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    String TAG = "iKOSMO";
    SharedPreferences.Editor editor;
    //전역변수
    EditText user_id, user_pw;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences pref = getSharedPreferences("login",
                Activity.MODE_PRIVATE);
        editor = pref.edit();

        /*
        getString()으로 저장된값을 가져와서 입력상자에 설정한다.
        만약 저장된 값이 없을경우에는 디폴트값으로 두번째 인자에
        지정된 값을 사용한다.
         */
        String id = pref.getString("id","");
        String pwd = pref.getString("pwd","");
        //위젯얻어오기
        user_id = (EditText)findViewById(R.id.user_id);//아이디 입력상자
        user_pw = (EditText)findViewById(R.id.user_pw);//패스워드 입력상자

        user_id.setText(id);
        user_pw.setText(pwd);


        Button btnLogin = (Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                execute()를 통해 doInBackground()를 호출한다.
                이때 전달하는 파라미터는 총 3개이다.
                첫번째는 요청URL, 두번째와 세번째는 서버로 전송할 파라미터이다.
                각 입력상자에 입력된 내용을 얻어와서 전달한다.
                 */String addr = getString(R.string.server_addr);
                new AsyncHttpRequest().execute(
                        addr+"memberLogin.do",
                        "id="+user_id.getText().toString(),
                        "pw="+user_pw.getText().toString()
                );


            }
        });
        //진행대화상자 준비...
        dialog = new ProgressDialog(this);
        dialog.setCancelable(true);//Back버튼을 누를때 대화상자창이 닫히게 설정
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("로그인 처리중");
        dialog.setMessage("서버로부터 응답을 기다리고 있습니다.");
    }

    class AsyncHttpRequest extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //응답대기 대화창
            if(!dialog.isShowing())
                dialog.show();
        }

        /*
        execute()호출시 전달된 3개의 파라미터를 가변인자로 받아서 사용함.
         */
        @Override
        protected String doInBackground(String... strings) {

            StringBuffer receiveData = new StringBuffer();
            try{
                URL url = new URL(strings[0]);//파라미터1 : 요청 URL
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();

                out.write(strings[1].getBytes());//파라미터2 : 사용자아이디
                out.write("&".getBytes());//&를 사용하여 쿼리스트링 형태로 만들어준다.
                out.write(strings[2].getBytes());//파라미터3 : 사용자패스워드

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
                {"isLogin":1,
                "memberInfo":
                    {"id":"dkanehdd","pass":"1111","name":"남민우","regidate":"2020-12-03"}
                }
                 */
                //JSON객체를 파싱
                JSONObject jsonObject = new JSONObject(s);
                int success = Integer.parseInt(jsonObject.getString("isLogin"));

                //파싱후 로그인 성공인 경우
                if(success==1){
                    sb.append("로그인 성공^^\n");

                    //객체안에 또 하나의 객체가 있으므로 getJSONObject()를 통해 파싱
                    String flag = jsonObject.getJSONObject("memberInfo").getString("flag").toString();
                    String name = jsonObject.getJSONObject("memberInfo").getString("name").toString();
                    sb.append(name+"님 환영합니다~^^");
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            sb.toString(),
                            Toast.LENGTH_SHORT).show();
                    editor.putString("id", user_id.getText().toString());
                    editor.putString("pwd", user_pw.getText().toString());
                    //저장후 반드시 commit()을 호출해야한다.
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this,
                            MenuList.class);
                    //부가데이터를 넘기기 위한 준비. Map컬렉션같이 Key와 value로 설정
                    intent.putExtra("id", user_id.getText().toString());
                    intent.putExtra("flag", flag);
                    //액티비티 실행
                    startActivity(intent);
                }
                else{
                    sb.append("아이디 또는 패스워드가 잘못되었습니다.");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            dialog.dismiss();
            Toast.makeText(getApplicationContext(),
                    sb.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}