package com.kosmo.slowwalking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SubMenuFragment1 extends Fragment {
    public static final String TAG = "iKosmo";

    ArrayList<String> sitter_id = new ArrayList<>();
    ArrayList<String> image_view = new ArrayList<String>() ;
    ArrayList<String> requestname = new ArrayList<String>();
    ArrayList<String> requestaddress = new ArrayList<String>();
    ArrayList<Integer> requestage = new ArrayList<>();
    ArrayList<Integer> requestaccount = new ArrayList<>();
    ArrayList<Integer> requeststarrate = new ArrayList<>();

    private static CustomAdapter customAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "MenuFragement1 > onCreateView()");

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.menu_subfragment2, container, false);


        /*Bundle bundle = getArguments();
        sitter_id = bundle.getStringArrayList("sitter_id");
        image_view = bundle.getStringArrayList("image_path");
        requestname = bundle.getStringArrayList("name");
        requestaddress = bundle.getStringArrayList("residence1");
        requestage = bundle.getIntegerArrayList("age");
        requestaccount = bundle.getIntegerArrayList("pay");
        requeststarrate = bundle.getIntegerArrayList("starrate");


        ArrayList<SitterListDTO> siter = new ArrayList<SitterListDTO>();
        for(int i=0; i<sitter_id.size() ; i++){
            SitterListDTO dto = new SitterListDTO();
            dto.setImage_path(image_view.get(i));
            dto.setName(requestname.get(i));
            dto.setResidence1(requestaddress.get(i));
            dto.setAge(requestage.get(i));
            dto.setPay(requestaccount.get(i));
            dto.setStarrate(requeststarrate.get(i));
            siter.add(dto);
        }

        ListView customListView = (ListView) viewGroup.findViewById(android.R.id.list);
        customAdapter  = new SubMenuFragment1.CustomAdapter(getContext(), siter);
        customListView.setAdapter(customAdapter);

*/

        return viewGroup;

    }
    public class CustomAdapter extends ArrayAdapter {

        private Context context;
        private List list;


        class ViewHolder {
            public TextView image_view;
            public TextView requestname;
            public TextView requestaddress;
            public TextView requestage;
            public TextView requestaccount;
            public TextView requeststarrate;
            public Button diary;
        }

        public CustomAdapter(Context context, ArrayList list){
            super(context, 0, list);
            this.context = context;
            this.list = list;
        }

        @SuppressLint("WrongViewCast")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null){
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                convertView = layoutInflater.inflate(R.layout.sitterlist, parent, false);
            }

            viewHolder = new SubMenuFragment1.CustomAdapter.ViewHolder();
            viewHolder.image_view = (TextView) convertView.findViewById(R.id.imageview);
            viewHolder.requestname = (TextView) convertView.findViewById(R.id.request_name);
            viewHolder.requestaddress = (TextView) convertView.findViewById(R.id.request_address);
            viewHolder.requestage = (TextView) convertView.findViewById(R.id.request_age);
            viewHolder.requestaccount = (TextView) convertView.findViewById(R.id.request_account);
            viewHolder.requeststarrate = (TextView) convertView.findViewById(R.id.request_starrate);
            viewHolder.diary = (Button) convertView.findViewById(R.id.Btndiary);

            final SitterListDTO dto = (SitterListDTO) list.get(position);
            viewHolder.image_view.setText(dto.getImage_path());
            viewHolder.requestname.setText(dto.getName());
            viewHolder.requestaddress.setText(dto.getResidence1());
            viewHolder.requestage.setText(dto.getAge());
            viewHolder.requestaccount.setText(dto.getPay());
            viewHolder.requeststarrate.setText(dto.getStarrate());
            viewHolder.diary.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),
                            WriteDiary.class);
                   // intent.putExtra("name", dto.getParents_name());
                    //intent.putExtra("idx", dto.getRequest_idx());
                    //액티비티 실행
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }




}
