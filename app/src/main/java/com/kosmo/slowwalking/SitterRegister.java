package com.kosmo.slowwalking;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class SitterRegister extends AppCompatActivity {


    Calendar calendar;//켈린더 클래스는 날짜, 시간을 생성하는 역할
    TextView date_tv, time_tv;//선택한 날짜, 시간을 표시
    int yearStr, monthStr, dayStr;
    int hourStr, minuteStr, secondStr;

    String TAG = "iKOSMO";
    private Button btn_sitterregister;
    EditText id, pw, pw2, name, phone, birthday, email;
    RadioGroup gender;
    TextView textResult;
    ProgressDialog dialog;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_register);

        id = (EditText) findViewById(R.id.user_id);//아이디 입력 상자
        pw = (EditText) findViewById(R.id.user_pw);//패스워드 입력 상자
        pw2 = (EditText) findViewById(R.id.user_pw2);//패스워드 입력 상자
        name = (EditText) findViewById(R.id.user_name);//패스워드 입력 상자
        phone = (EditText) findViewById(R.id.user_phone);//패스워드 입력 상자
        gender = (RadioGroup) findViewById(R.id.user_gender);//패스워드 입력 상자


        ///날짜 가져오기
        //텍스트 뷰 얻어오기
        date_tv = (TextView)findViewById(R.id.date_tv);
        email = (EditText) findViewById(R.id.user_email);//패스워드 입력 상자
        btn_sitterregister = (Button) findViewById(R.id.btn_sitterregister);//패스워드 입력 상자

        Button btnLogin = (Button) findViewById(R.id.btn_sitterregister);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                execute()를 통해 doInBackground()를 호출한다.
                이때 전달하는 파라미터는 총 3개이다.
                첫번째는 요청URL, 두번째와 세번째는 서버로 전송할 파라미터이다.
                각 입력상자에 입력된 내용을 가져와서 전달한다.
                 */
                new AsyncHttpServer().execute(
                        "http://192.168.219.112:8080/slowwalking/android/joinAction",

                        "id=" + id.getText().toString(),
                        "pw=" + pw.getText().toString(),
                        "name=" + name.getText().toString(),
                        "phone=" + phone.getText().toString(),
                        "gender=" + gender.getCheckedRadioButtonId(),
                        "birthday=" + date_tv.getText().toString(),
                        "email=" + email.getText().toString(),
                        "flag=sitter"

                );
            }
        });

        pw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String pw1 = pw.getText().toString();
                String pwc = pw2.getText().toString();

                if(pw1.equals(pwc)){
                    pw.setBackgroundColor(Color.GREEN);
                    pw2.setBackgroundColor(Color.GREEN);

                }else {
                    pw.setBackgroundColor(Color.RED);
                    pw2.setBackgroundColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        btn_sitterregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(gender.getCheckedRadioButtonId()==0){
                    Toast.makeText(SitterRegister.this,"성별을 선택해주세요",
                            Toast.LENGTH_SHORT).show();
                    gender.requestFocus();
                    return;
                }

                if(name.getText().toString().length()==0){
                    Toast.makeText(SitterRegister.this,"이름 입력해주세요",
                            Toast.LENGTH_SHORT).show();
                    name.requestFocus();
                    return;
                }
                
                if(id.getText().toString().length()==0){
                    Toast.makeText(SitterRegister.this,"아이디를 입력하세요",
                            Toast.LENGTH_SHORT).show();

                    id.requestFocus();
                    return;
                }
                if(pw.getText().toString().length()==0){
                    Toast.makeText(SitterRegister.this,"비밀번호를 입력해주세요",
                            Toast.LENGTH_SHORT).show();
                    pw.requestFocus();
                    return;
                }
                if(pw2.getText().toString().length()==0){
                    Toast.makeText(SitterRegister.this,"비밀번호 확인해주세요",
                            Toast.LENGTH_SHORT).show();
                    pw.requestFocus();
                    return;
                }
                if(!pw.getText().toString().equals(pw2.getText().toString())){
                    Toast.makeText(SitterRegister.this,"비밀번호가 일치하지않습니다",
                            Toast.LENGTH_SHORT).show();
                    pw.setText("");
                    pw2.setText("");
                    pw.requestFocus();
                    return;
                }
                if(email.getText().toString().length()==0){
                    Toast.makeText(SitterRegister.this,"이메일을 입력해주세요",
                            Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                    return;
                }
                if(phone.getText().toString().length()==0){
                        Toast.makeText(SitterRegister.this,"전화번호를 입력해주세요",
                            Toast.LENGTH_SHORT).show();
                    phone.requestFocus();
                    return;
                }
                if(date_tv.getText().toString().length()==0){
                    Toast.makeText(SitterRegister.this,"생년월일을 선택해주세요",
                            Toast.LENGTH_SHORT).show();
                    date_tv.requestFocus();
                    return;
                }
            }
        });



        //날짜와 시간을 다루는 클래스면 무엇이든 사용가능
        calendar = Calendar.getInstance(); //생성자가 없는 클래스이므로 유틸메소드로 객체생성
        yearStr = calendar.get(calendar.YEAR);
        monthStr = calendar.get(calendar.MONTH);
        dayStr = calendar.get(calendar.DATE);
        hourStr = calendar.get(calendar.HOUR_OF_DAY);
        minuteStr = calendar.get(calendar.MINUTE);
        secondStr = calendar.get(calendar.SECOND);


        //날짜 선택 버튼에 리스너 부착
        Button btn_datepicker = (Button) findViewById(R.id.btn_datepicker);
        btn_datepicker.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  DatePickerDialog dialog = new DatePickerDialog(
                          v.getContext(),
                          listener,
                          yearStr, monthStr, dayStr
                  );
                  dialog.show();//객체생성후 show()함수를 통해 출력한다.
              }
          }
        );

    }

    //데이트피커에서 날짜를 선택한후 확인버튼을 눌렀을때의 리스너 선언
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            //선택한 날짜를 텍스트뷰에 설정함
            date_tv.setText(String.format("%d-%d-%d",
                    year, (month + 1), dayOfMonth));
            /*
            Calendar클래스를 통해 월을 반환받으면 0~11까지이므로
            +1 해줘야 현재 월이 출력된다.
             */
            //선택한 날짜를 토스트로 띄워줌
            Toast.makeText(getApplicationContext(),
                    year + "년" + (month + 1) + "월" + dayOfMonth + "일",
                    Toast.LENGTH_LONG).show();
        }
    };

    class AsyncHttpServer extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /*
        execute()호출시 전달된 3개의 파라미터를 가변인자로 받아서 사용함.
         */
        @Override
        protected String doInBackground(String... strings) {

            StringBuffer receiveData = new StringBuffer();

            try {
                URL url = new URL(strings[0]);//파라미터1 : 요청URL
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream out = conn.getOutputStream();
                out.write(strings[1].getBytes());//파라미터2 : 아이디
                out.write("&".getBytes());
                out.write(strings[2].getBytes());//파라미터3 : 패스워드
                out.write("&".getBytes());
                out.write(strings[3].getBytes());//파라미터4 :
                out.write("&".getBytes());
                out.write(strings[4].getBytes());//파라미터5 :
                out.write("&".getBytes());
                out.write(strings[5].getBytes());//파라미터6 :
                out.write("&".getBytes());
                out.write(strings[6].getBytes());//파라미터7 :
                out.write("&".getBytes());
                Log.i(TAG,strings[7]);
                out.write(strings[7].getBytes());//파라미터8 :
                out.write("&".getBytes());
                Log.i(TAG,strings[8]);
                out.write(strings[8].getBytes());//파라미터8 :

                out.flush();
                out.close();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    //스프링 서버에 연결성공한 경우 JSON데이터를 읽어서 저장한다.
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "UTF-8")
                    );
                    String data;
                    while ((data = reader.readLine()) != null) {
                        receiveData.append(data + "\r\n");
                    }
                    reader.close();
                } else {
                    Log.i(TAG, conn.getResponseCode()+"");
                    Log.i(TAG, "HTTP_OK 안됨. 연결실패.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //저장된 내용을 로그로 출력한후 onPostExecute()로 반환한다.
            Log.i(TAG, receiveData.toString());
            return receiveData.toString();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        //doInBackgound()에서 반환한 값은 해당 메소드로 전달하다.
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }







}


