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

public class MenuFragment1 extends Fragment {

    public static final String TAG = "ikosmo";
    String[] idolGroup = {"엑소","방탄소년단","워너원","뉴이스트","갓세븐","비투비","빅스"};

    int[] teamCount ={9,7,11,5,7,7,6};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "MenuFragement1 > onCreateView()");

        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.menu_fragment1, container, false);
        ListView listView = (ListView)viewGroup.findViewById(R.id.listview1);
        MyAdapter myAdapter = new MyAdapter();
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
    class MyAdapter extends BaseAdapter{
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
