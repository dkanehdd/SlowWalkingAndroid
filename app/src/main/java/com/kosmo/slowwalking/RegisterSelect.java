package com.kosmo.slowwalking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_select);

        ((Button)findViewById(R.id.ParentsRegister)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),
                        ParentsRegister.class);
                //액티비티 실행
                startActivity(intent);
            }
        });


        ((Button)findViewById(R.id.SitterRegister)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),
                        SitterRegister.class);
                //액티비티 실행
                startActivity(intent);
            }
        });
    }


}