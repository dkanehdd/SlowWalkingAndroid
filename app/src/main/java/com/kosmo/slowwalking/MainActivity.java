package com.kosmo.slowwalking;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {
    final String TAG = "lecture";
    String regId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if(intent != null && intent.getExtras() != null){
            for(String key : getIntent().getExtras().keySet()){
                String value = getIntent().getExtras().getString(key);
                Log.d(TAG, "Noti - "+key +" : "+ value);
            }
        }
        Log.d(TAG, "알림메시지가 있어요");
        String contents = intent.getStringExtra("message");
        if(contents!=null){
            processIntent(contents);
        }
        getRegistrationId();

        Button login = (Button)findViewById(R.id.btnLogin);
        Log.i("SLOW",Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),
                        LoginActivity.class);
                //액티비티 실행
                startActivity(intent);
            }
        });
        ((Button)findViewById(R.id.btnregister)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),
                        RegisterSelect.class);
                //액티비티 실행
                startActivity(intent);
            }
        });
    }

    public void getRegistrationId(){
        Log.d(TAG,"getRegistrationId() 호출됨.");

        regId = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "RegId : "+ regId);
        println("regId : "+ regId);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent != null){
            String contents = intent.getStringExtra("message");
            processIntent(contents);
        }
    }

    private void processIntent(String contents){
        println("DATA:"+ contents);
    }
    public void println(String data){
    }
}