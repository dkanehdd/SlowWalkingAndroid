package com.kosmo.slowwalking;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ParentsRegister extends AppCompatActivity {
    Calendar calendar;//켈린더 클래스는 날짜, 시간을 생성하는 역할
    TextView date_tv, time_tv;//선택한 날짜, 시간을 표시
    int yearStr, monthStr, dayStr;
    int hourStr, minuteStr, secondStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_register);


        //텍스트 뷰 얻어오기
        date_tv = (TextView)findViewById(R.id.date_tv);


        //날짜와 시간을 다루는 클래스면 무엇이든 사용가능
        calendar = Calendar.getInstance(); //생성자가 없는 클래스이므로 유틸메소드로 객체생성
        yearStr = calendar.get(calendar.YEAR);
        monthStr = calendar.get(calendar.MONTH);
        dayStr = calendar.get(calendar.DATE);
        hourStr = calendar.get(calendar.HOUR_OF_DAY);
        minuteStr = calendar.get(calendar.MINUTE);
        secondStr = calendar.get(calendar.SECOND);


        //날짜 선택 버튼에 리스너 부착
        Button btn_datepicker = (Button)findViewById(R.id.btn_datepicker);
        btn_datepicker.setOnClickListener(new View.OnClickListener(){
                                              @Override
                                              public void onClick(View v) {
                    /*
                    버튼을 눌렀을때 데이트피커 대화창을 띄워준다.
                    형식]
                        new DatePickerDialog(
                            대화창을 띄울 컨텍스트, 리스너
                            설정할 년, 월, 일
                        );
                        리스너는 내부 외부 어디든 설정 가능함.
                     */
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
                    year, (month+1), dayOfMonth));
            /*
            Calendar클래스를 통해 월을 반환받으면 0~11까지이므로
            +1 해줘야 현재 월이 출력된다.
             */
            //선택한 날짜를 토스트로 띄워줌
            Toast.makeText(getApplicationContext(),
                    year+"년"+(month+1)+"월"+dayOfMonth+"일",
                    Toast.LENGTH_LONG).show();
        }
    };
}
