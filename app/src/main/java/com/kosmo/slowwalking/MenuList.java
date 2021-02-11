package com.kosmo.slowwalking;

import android.content.ClipData;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MenuList extends AppCompatActivity {

    public static final String TAG = "iKosmo";

    String flag;
    FragmentManager fragmentManager;
    MenuFragment1 menuFragment1;
    MenuFragment2 menuFragment2;
    MenuFragment3 menuFragment3;
    MenuFragment4 menuFragment4;
    MenuFragment5 menuFragment5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        final Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        if(flag.equals("sitter")){

        }


        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        menuFragment1 = new MenuFragment1();
        menuFragment2 = new MenuFragment2();
        menuFragment3 = new MenuFragment3();
        menuFragment4 = new MenuFragment4();
        menuFragment5 = new MenuFragment5();

        fragmentTransaction.replace(R.id.frameLayout, menuFragment1).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.homeItem:
                    transaction.replace(R.id.frameLayout, menuFragment1).commitAllowingStateLoss();
                    break;
                case R.id.interviewItem:
                    transaction.replace(R.id.frameLayout, menuFragment2).commitAllowingStateLoss();
                    break;
                case R.id.dialyItem:
                    transaction.replace(R.id.frameLayout, menuFragment3).commitAllowingStateLoss();
                    break;
                case R.id.mypageItem:
                    transaction.replace(R.id.frameLayout, menuFragment4).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}