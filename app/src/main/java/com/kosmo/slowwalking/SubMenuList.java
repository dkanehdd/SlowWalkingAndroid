        package com.kosmo.slowwalking;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SubMenuList extends AppCompatActivity {


    public static final String TAG = "iKosmo";

    FragmentManager fragmentManager;
    SubMenuFragment1 submenuFragment1;
    SubMenuFragment2 submenuFragment2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_menu_list);

        Button button1 = (Button)findViewById(R.id.btnsubFirstFragment);
        Button button2 = (Button)findViewById(R.id.btnsubSecondFragment);

        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);


        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        submenuFragment1 = new SubMenuFragment1();
        submenuFragment2 = new SubMenuFragment2();

        fragmentTransaction.replace(R.id.mainLayout, submenuFragment1).commit();
    }

    View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.btnsubFirstFragment){
                fragmentManager.beginTransaction().replace(R.id.mainLayout, submenuFragment1).commit();
            }
            else if(view.getId()==R.id.btnsubSecondFragment) {
                fragmentManager.beginTransaction().replace(R.id.mainLayout, submenuFragment2).commit();
            }
        }
    };
}