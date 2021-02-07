package com.kosmo.slowwalking;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuList extends AppCompatActivity {

    public static final String TAG = "iKosmo";

    FragmentManager fragmentManager;
    MenuFragment1 menuFragment1;
    MenuFragment2 menuFragment2;
    MenuFragment3 menuFragment3;
    MenuFragment4 menuFragment4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        Button button1 = (Button)findViewById(R.id.btnFirstFragment);
        Button button2 = (Button)findViewById(R.id.btnSecondFragment);
        Button button3 = (Button)findViewById(R.id.btnThirdFragment);
        Button button4 = (Button)findViewById(R.id.btnFourthFragment);
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        button4.setOnClickListener(listener);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        menuFragment1 = new MenuFragment1();
        menuFragment2 = new MenuFragment2();
        menuFragment3 = new MenuFragment3();
        menuFragment4 = new MenuFragment4();

        fragmentTransaction.replace(R.id.mainLayout, menuFragment1).commit();
    }

    View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.btnFirstFragment){
                fragmentManager.beginTransaction().replace(R.id.mainLayout, menuFragment1).commit();
            }
            else if(view.getId()==R.id.btnSecondFragment){
                fragmentManager.beginTransaction().replace(R.id.mainLayout, menuFragment2).commit();
            }
            else if(view.getId()==R.id.btnThirdFragment){
                fragmentManager.beginTransaction().replace(R.id.mainLayout, menuFragment3).commit();
            }
            else if(view.getId()==R.id.btnFourthFragment){
                fragmentManager.beginTransaction().replace(R.id.mainLayout, menuFragment4).commit();
            }
        }
    };
}