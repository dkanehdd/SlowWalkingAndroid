package com.kosmo.slowwalking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MenuFragment5 extends Fragment {

    public static final String TAG = "ikosmo";
    String[] idolGroup = {"프로필수정 ","내 신청서작성","내 신청서","내 구직현황","알림장","후기관리","이용권","포인트"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "MenuFragement5 > onCreateView()");

        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.menu_fragment5, container, false);
        ListView listView = (ListView)viewGroup.findViewById(R.id.listview5);
        MenuFragment5.MyAdapter myAdapter = new MenuFragment5.MyAdapter();
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "인덱스 : "+position);
                Toast.makeText(getContext(),
                        idolGroup[position]+" 선택됨",
                        Toast.LENGTH_SHORT).show();
            }
        });
        return viewGroup;
    }
    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return idolGroup.length;
        }

        @Override
        public Object getItem(int position) {
            return idolGroup[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            IdolView idolView = new IdolView(getContext());
            idolView.setName(idolGroup[position]);

            return idolView;

        }
    }
    
}

