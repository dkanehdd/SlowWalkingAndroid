package com.kosmo.slowwalking;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MenuFragment1 extends Fragment {

    public static final String TAG = "iKosmo";

    SubMenuFragment1 submenuFragment1;
    SubMenuFragment2 submenuFragment2;
    FragmentTransaction mFragmentTransaction;
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



        mFragmentTransaction = getChildFragmentManager().beginTransaction();
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

        BottomNavigationView bottomNavigationView = viewGroup.findViewById(R.id.navigationView1);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());//하단바에 리스너 부착


        return viewGroup;

    }

    //리스너 설정
    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch(menuItem.getItemId())
            {
                case R.id.btnsubFirstFragment:
                    getChildFragmentManager().beginTransaction().replace(R.id.mainLayout, submenuFragment1).commit();
                    break;
                case R.id.btnsubSecondFragment:
                    getChildFragmentManager().beginTransaction().replace(R.id.mainLayout, submenuFragment2).commit();
                    break;
            }
            return true;
        }
    }


}
