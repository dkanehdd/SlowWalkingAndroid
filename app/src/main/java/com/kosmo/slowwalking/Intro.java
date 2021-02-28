package com.kosmo.slowwalking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class Intro extends AppCompatActivity {

    /*
    Manifest.xml 파일을 수정하여 Intro액티비티가 가장먼저
    실행되도록 한다.
    */
    //일정시간이후에 실행하기위해 Handler객체를 생성한다.
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //인트로 화면 이후에 메인액티비티를 실행하기위해 인텐트 객체생성
            Intent intent = new Intent(getApplicationContext(),
                    MainActivity.class);
            //액티비티 실행
            startActivity(intent);

            /*
            액티비티가 실행되거나 종료될때 애니메이션 효과를 부여한다.
            인자1 : 새롭게실행되는 액티비티의 애니메이션
            인자2 : 종료되는 액티비티에 적용
             */
            overridePendingTransition(R.anim.slide_in_left,
                    R.anim.hold);
            
            finish();
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    //액티비티 실행시 3번째로 실행되는 수명주기 함수
    @Override
    protected void onResume() {
        super.onResume();
        //Intro화면에 진입한 후 2초후에 runnable객체를 실행한다.
        handler.postDelayed(runnable, 2000);
    }
    //액티비티종료시 첫번째로 실행되는 수명주기 함수
    @Override
    protected void onPause() {
        //Intro가 종료될때 handler에 예약해놓은 작업을 취소한다.
        super.onPause();
        
        //handler.removeCallbacks(runnable);
    }
}