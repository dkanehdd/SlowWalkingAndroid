package com.kosmo.slowwalking;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
import java.util.ArrayList;
import java.util.List;

public class MenuFragment1 extends Fragment {

    public static final String TAG = "iKosmo";

    SubMenuFragment1 submenuFragment1;
    SubMenuFragment2 submenuFragment2;

    String id;
    String user_id;
    String flag;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        id = bundle.getString("sitter_id");
        user_id = bundle.getString("id");
        flag = bundle.getString("flag");



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "MenuFragement1 > onCreateView()");

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.menu_fragment1, container, false);
       // ListView listView = (ListView) viewGroup.findViewById(R.id.listview1);


        Button button1 = (Button)viewGroup.findViewById(R.id.btnsubFirstFragment);
        Button button2 = (Button)viewGroup.findViewById(R.id.btnsubSecondFragment);
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);

        FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();
        submenuFragment1 = new SubMenuFragment1();
        submenuFragment2 = new SubMenuFragment2();
        Bundle bundle2 = new Bundle();
        bundle2.putString("id", user_id);
        bundle2.putString("flag",flag);
        submenuFragment1.setArguments(bundle2);
        Bundle bundle3 = new Bundle();
        bundle3.putString("id", user_id);
        bundle3.putString("flag",flag);
        submenuFragment2.setArguments(bundle3);
        getChildFragmentManager().beginTransaction().replace(R.id.mainLayout, submenuFragment1).commit();



        return viewGroup;

    }

    View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.btnsubFirstFragment){
                getChildFragmentManager().beginTransaction().replace(R.id.mainLayout, submenuFragment1).commit();
            }
            else if(view.getId()==R.id.btnsubSecondFragment){
                getChildFragmentManager().beginTransaction().replace(R.id.mainLayout, submenuFragment2).commit();
            }
        }
    };


}
