package com.kosmo.slowwalking;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MenuFragment2 extends ListFragment {


    public static final String TAG = "ikosmo";
    ArrayList<String> interviewID;
    ArrayList<String> request_time;
    ArrayList<Integer> interview_idx;
    String flag;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "MenuFragement2 > onCreateView()");


        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.menu_fragment2, container, false);
        ListView listView = (ListView)viewGroup.findViewById(android.R.id.list);

        Bundle bundle = getArguments();
        interviewID = bundle.getStringArrayList("interviewID");
        request_time = bundle.getStringArrayList("request_time");
        interview_idx = bundle.getIntegerArrayList("interview_idx");
        flag= bundle.getString("flag");

        MyAdapter myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);



        return viewGroup;
    }
    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return interviewID.size();
        }

        @Override
        public Object getItem(int position) {
            return interviewID.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            InterView interView = new InterView(getContext());
            interView.setName(interviewID.get(position));
            interView.setPhone(request_time.get(position));
            interView.setAgree(interview_idx.get(position), flag);
            interView.setDelete(interview_idx.get(position), flag);
            return interView;

        }
    }

}

