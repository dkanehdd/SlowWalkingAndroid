package com.kosmo.slowwalking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class MenuFragment3 extends Fragment {

    public static final String TAG = "iKosmo";

    ArrayList<Integer> diary_idx = new ArrayList<Integer>();
    ArrayList<String> Request_time = new ArrayList<String>() ;
    ArrayList<String> parentsName = new ArrayList<String>();
    private static CustomAdapter customAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "MenuFragment3 > onCreateView()");
        View rootView = inflater.inflate(R.layout.menu_fragment3, container, false);

        Bundle bundle = getArguments();
        Request_time = bundle.getStringArrayList("request_time");
        parentsName = bundle.getStringArrayList("parentsName");
        diary_idx = bundle.getIntegerArrayList("diary_idx");
        ArrayList<InterviewDTO> inter = new ArrayList<InterviewDTO>();
        for(int i=0; i<diary_idx.size() ; i++){
            InterviewDTO dto = new InterviewDTO();
            dto.setRequest_time(Request_time.get(i));
            dto.setParents_name(parentsName.get(i));
            dto.setRequest_idx(diary_idx.get(i));
            inter.add(dto);
        }

        ListView customListView = (ListView) rootView.findViewById(android.R.id.list);
        customAdapter  = new CustomAdapter(getContext(), inter);
        customListView.setAdapter(customAdapter);
        return rootView;
    }

    public class CustomAdapter extends ArrayAdapter{

        private Context context;
        private List list;


        class ViewHolder {
            public TextView parentname;
            public TextView time;
            public Button diary;
        }

        public CustomAdapter(Context context, ArrayList list){
            super(context, 0, list);
            this.context = context;
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null){
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                convertView = layoutInflater.inflate(R.layout.diary_view, parent, false);
            }

            viewHolder = new ViewHolder();
            viewHolder.parentname = (TextView) convertView.findViewById(R.id.parent_name);
            viewHolder.time = (TextView) convertView.findViewById(R.id.request_time);
            viewHolder.diary = (Button) convertView.findViewById(R.id.Btndiary);

            final InterviewDTO dto = (InterviewDTO) list.get(position);
            viewHolder.parentname.setText(dto.getParents_name());
            viewHolder.time.setText(dto.getRequest_time());
            viewHolder.diary.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),
                            WriteDiary.class);
                    intent.putExtra("name", dto.getParents_name());
                    intent.putExtra("idx", dto.getRequest_idx());
                    //액티비티 실행
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }


}

