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


    ArrayList<String> sitter_id = new ArrayList<>();
    ArrayList<String> image_view = new ArrayList<String>() ;
    ArrayList<String> requestname = new ArrayList<String>();
    ArrayList<String> requestaddress = new ArrayList<String>();
    ArrayList<Integer> requestage = new ArrayList<>();
    ArrayList<Integer> requestaccount = new ArrayList<>();
    ArrayList<Integer> requeststarrate = new ArrayList<>();


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


        Bundle bundle = getArguments();
        sitter_id = bundle.getStringArrayList("sitter_id");
        image_view = bundle.getStringArrayList("image_path");
        requestname = bundle.getStringArrayList("name");
        requestaddress = bundle.getStringArrayList("residence1");
        requestage = bundle.getIntegerArrayList("age");
        requestaccount = bundle.getIntegerArrayList("pay");
        requeststarrate = bundle.getIntegerArrayList("starrate");

        submenuFragment1.setArguments(bundle);//프래그먼트에 세팅






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
