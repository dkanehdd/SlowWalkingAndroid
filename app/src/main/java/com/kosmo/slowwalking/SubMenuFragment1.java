package com.kosmo.slowwalking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SubMenuFragment1 extends Fragment {
    public static final String TAG = "iKosmo";

    ListView customListView;

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
    ArrayList<SitterListDTO> siter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SitterList().execute( //시터 리스트 불러오기
                "http://192.168.219.121:8080/slowwalking/android/SitterBoard_list"

        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "MenuFragement1 > onCreateView()");

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.menu_subfragment1, container, false);



        customListView = (ListView) viewGroup.findViewById(android.R.id.list);

        Bundle bundle = getArguments();

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
    class SitterList extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            StringBuffer receiveData = new StringBuffer();
            try{
                URL url = new URL(strings[0]);//파라미터1 : 요청 URL
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                out.flush();
                out.close();

                if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    Log.i(TAG, "HTTP OK 성공");

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(),"UTF-8"));
                    String responseData;
                    while((responseData=reader.readLine())!=null){
                        receiveData.append(responseData+"\n\r");
                    }
                    reader.close();
                }
                else{
                    Log.i(TAG, connection.getResponseCode()+"");
                    Log.i(TAG, "HTTP OK 안됨");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            //저장된 내용을 로그로 출력한후 onPostExecute()로 반환한다.
            Log.i(TAG, receiveData.toString());
            //서버에서 내려준 JSON정보를 저장후 반환
            return receiveData.toString();
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            StringBuffer sb = new StringBuffer();
            try{
                /*
                {"lists":[{"idx":8,"parents_id":"dkanehdd","sitter_id":"dkanehd","request_time":"15:00 ~ 20:00",
                "parents_agree":"T","sitter_agree":"T","request_idx":10,"parents_name":null,"sitter_name":null},
                {"idx":7,"parents_id":"kosmo3","sitter_id":"dkanehd","request_time":"18:00 ~ 21:00","parents_agree":"T",
                "sitter_agree":"T","request_idx":9,"parents_name":null,"sitter_name":null}]}
                 */
                //JSON객체를 파싱
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = (JSONArray)jsonObject.get("lists");//lists로 배열을 먼저 얻어옴 []
                siter = new ArrayList<SitterListDTO>();
                for(int i=0 ; i<jsonArray.length() ; i++){//배열크기만큼반복
                    JSONObject sitterview = (JSONObject) jsonArray.get(i); //배열에서 하나씩 가져옴
                    SitterListDTO dto = new SitterListDTO();
                    dto.setName(sitterview.get("name").toString());//가져와서 컬렉션에 저장
                    dto.setResidence1(sitterview.get("residence1").toString());
                    dto.setImage_path(sitterview.get("image_path").toString());
                    dto.setAge(Integer.parseInt(sitterview.get("age").toString()));
                    dto.setPay(Integer.parseInt(sitterview.get("pay").toString()));
                    dto.setStarrate(Integer.parseInt(sitterview.get("starrate").toString()));
                    siter.add(dto);
                }
                customAdapter  = new SubMenuFragment1.CustomAdapter(getContext(), siter);
                customListView.setAdapter(customAdapter);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }



}
