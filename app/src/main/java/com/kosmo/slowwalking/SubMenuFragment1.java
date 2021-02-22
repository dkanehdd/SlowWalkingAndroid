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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
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

    //시터리스트 상세
    ArrayList<String> requestintro = new ArrayList<String>();
    ArrayList<String> requestcctv = new ArrayList<String>();
    ArrayList<String> requestpersnal = new ArrayList<String>();
    ArrayList<String> requestlicense = new ArrayList<String>();
    ArrayList<String> requestactivity = new ArrayList<String>();
    ArrayList<String> requestdate = new ArrayList<String>();
    ArrayList<String> requestaddress2 = new ArrayList<String>();
    ArrayList<String> requestaddress3 = new ArrayList<String>();
    ArrayList<String> requestgender = new ArrayList<>();

    private CustomAdapter customAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "MenuFragement1 > onCreateView()");

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.menu_subfragment1, container, false);


        Bundle bundle = getArguments();
        sitter_id = bundle.getStringArrayList("sitter_id");
        image_view = bundle.getStringArrayList("image_path");
        requestname = bundle.getStringArrayList("name");
        requestaddress = bundle.getStringArrayList("residence1");
        requestage = bundle.getIntegerArrayList("age");
        requestaccount = bundle.getIntegerArrayList("pay");
        requeststarrate = bundle.getIntegerArrayList("starrate");

        requestintro = bundle.getStringArrayList("introduction");
        requestcctv = bundle.getStringArrayList("cctv_agree");
        requestpersnal = bundle.getStringArrayList("personality_check");
        requestlicense = bundle.getStringArrayList("license_check");
        requestactivity = bundle.getStringArrayList("activity_time");
        requestdate = bundle.getStringArrayList("activity_date");
        requestaddress2 = bundle.getStringArrayList("residence2");
        requestaddress3 = bundle.getStringArrayList("residence3");
        requestgender = bundle.getStringArrayList("gender");








        ArrayList<SitterListDTO> siter = new ArrayList<SitterListDTO>();
        for(int i=0; i<sitter_id.size() ; i++){
            SitterListDTO dto = new SitterListDTO();
            dto.setImage_path(image_view.get(i));
            dto.setName(requestname.get(i));
            dto.setResidence1(requestaddress.get(i));
            dto.setAge(requestage.get(i));
            dto.setPay(requestaccount.get(i));
            dto.setStarrate(requeststarrate.get(i));

            dto.setIntroduction(requestintro.get(i));
            dto.setCctv_agree(requestcctv.get(i));
            dto.setPersonality_check(requestpersnal.get(i));
            dto.setLicense_check(requestlicense.get(i));
            dto.setActivity_time(requestactivity.get(i));
            dto.setActivity_date(requestdate.get(i));
            dto.setResidence2(requestaddress2.get(i));
            dto.setResidence3(requestaddress3.get(i));
            dto.setGender(requestgender.get(i));
            siter.add(dto);
        }
        ListView customListView = (ListView) viewGroup.findViewById(android.R.id.list);
        customAdapter  = new SubMenuFragment1.CustomAdapter(getContext(), siter);
        customListView.setAdapter(customAdapter);




        return viewGroup;

    }
    public class CustomAdapter extends ArrayAdapter {

        private Context context;
        private List list;


        class ViewHolder {
            public ImageView image_view;
            public TextView requestname;
            public TextView requestaddress;
            public TextView requestage;
            public TextView requestaccount;
            public RatingBar requeststarrate;
            public Button diary;
        }

        public CustomAdapter(Context context, ArrayList list){
            super(context, 0, list);
            this.context = context;
            this.list = list;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null){
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                convertView = layoutInflater.inflate(R.layout.sitterlist, parent, false);
            }

            viewHolder = new SubMenuFragment1.CustomAdapter.ViewHolder();
            viewHolder.image_view = (ImageView) convertView.findViewById(R.id.imageview);
            viewHolder.requestname = (TextView) convertView.findViewById(R.id.request_name);
            viewHolder.requestaddress = (TextView) convertView.findViewById(R.id.request_address);
            viewHolder.requestage = (TextView) convertView.findViewById(R.id.request_age);
            viewHolder.requestaccount = (TextView) convertView.findViewById(R.id.request_account);
            viewHolder.requeststarrate = (RatingBar) convertView.findViewById(R.id.request_starrate);
            viewHolder.diary = (Button) convertView.findViewById(R.id.BtnsitterView);

            final SitterListDTO dto = (SitterListDTO) list.get(position);
            viewHolder.requestname.setText(dto.getName());
            viewHolder.requestaddress.setText(dto.getResidence1());
            viewHolder.requestage.setText(Integer.toString(dto.getAge()));
            viewHolder.requestaccount.setText(Integer.toString(dto.getPay()));
            viewHolder.requeststarrate.setRating(dto.getStarrate());
            viewHolder.diary.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),
                            InterViewDetail.class);
                   intent.putExtra("sitter_id", dto.getSitter_id());
                   intent.putExtra("image_path", dto.getImage_path());
                   intent.putExtra("name", dto.getName());
                   intent.putExtra("introduction", dto.getIntroduction());
                   intent.putExtra("cctv_agree", dto.getCctv_agree());
                   intent.putExtra("personality_check", dto.getPersonality_check());
                   intent.putExtra("license_check", dto.getLicense_check());
                   intent.putExtra("activity_time", dto.getActivity_time());
                   intent.putExtra("activity_date", dto.getActivity_date());
                   intent.putExtra("residence1", dto.getResidence1());
                   intent.putExtra("residence2", dto.getResidence2());
                   intent.putExtra("residence3", dto.getResidence3());
                   intent.putExtra("age", dto.getAge());
                   intent.putExtra("pay", dto.getPay());
                   intent.putExtra("gender", dto.getGender());
                   intent.putExtra("starrate", dto.getStarrate());
                    //액티비티 실행
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }




}
