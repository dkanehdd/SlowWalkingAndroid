package com.kosmo.slowwalking;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SubMenuFragment2 extends Fragment {

    public static final String TAG = "iKosmo";

    ListView customListView;
    String user_id;
    String flag;

    private CustomAdapter customAdapter;
    ArrayList<RequestBoardDTO> requestBoard;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String addr = getResources().getString(R.string.server_addr);
        Bundle bundle = getArguments();
        user_id = bundle.getString("id");
        flag = bundle.getString("flag");
        new RequestBoardList().execute( //의뢰리스트
                addr+"requestBoard_list"
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "MenuFragement1 > onCreateView()");

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.menu_subfragment2, container, false);



        customListView = (ListView) viewGroup.findViewById(android.R.id.list);






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
            public TextView title;
            public TextView region;
            public TextView children_name;
            public TextView age;
            public TextView request_pay;
            public TextView request_date;
            public TextView request_time;
            public RatingBar starrate;
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
                convertView = layoutInflater.inflate(R.layout.requestboard, parent, false);
            }

            viewHolder = new ViewHolder();
            viewHolder.image_view = (ImageView) convertView.findViewById(R.id.imageview);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.region = (TextView) convertView.findViewById(R.id.region);
            viewHolder.children_name = (TextView) convertView.findViewById(R.id.children_name);
            viewHolder.age = (TextView) convertView.findViewById(R.id.age);
            viewHolder.request_pay = (TextView) convertView.findViewById(R.id.request_pay);
            viewHolder.request_date = (TextView) convertView.findViewById(R.id.request_date);
            viewHolder.request_time = (TextView) convertView.findViewById(R.id.request_time);
            viewHolder.starrate = (RatingBar) convertView.findViewById(R.id.request_starrate);

            final RequestBoardDTO dto = (RequestBoardDTO) list.get(position);
            viewHolder.title.setText(dto.getTitle());
            viewHolder.region.setText("["+dto.getRegion()+"]");
            viewHolder.children_name.setText(dto.getChildren_name());
            viewHolder.age.setText(dto.getAge()+"세");
            viewHolder.request_pay.setText(dto.getPay());
            viewHolder.request_date.setText(dto.getRequest_date());
            viewHolder.request_time.setText(dto.getRequest_time());
            viewHolder.starrate.setRating(dto.getStarrate());
            Glide.with(context).load(dto.getImage()).into(viewHolder.image_view);
            return convertView;
        }
    }
    class RequestBoardList extends AsyncTask<String, Void, String> {


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
                JSONArray jsonArray = new JSONArray(s);//lists로 배열을 먼저 얻어옴 []
                requestBoard = new ArrayList<RequestBoardDTO>();
                for(int i=0 ; i<jsonArray.length() ; i++){//배열크기만큼반복
                    JSONObject sitterview = (JSONObject) jsonArray.get(i); //배열에서 하나씩 가져옴
                    RequestBoardDTO dto = new RequestBoardDTO();
                    dto.setId(sitterview.get("id").toString());
                    dto.setTitle(sitterview.get("title").toString());
                    dto.setRegion(sitterview.get("region").toString());//가져와서 컬렉션에 저장
                    dto.setChildren_name(sitterview.get("children_name").toString());
                    dto.setAge(sitterview.get("age").toString());
                    dto.setPay(sitterview.get("pay").toString());
                    dto.setRequest_date(sitterview.get("request_date").toString());
                    dto.setRequest_time(sitterview.get("request_time").toString());
                    dto.setStarrate(Integer.parseInt(sitterview.get("starrate").toString()));
                    dto.setIdx(Integer.parseInt(sitterview.get("idx").toString()));
                    dto.setImage("http://192.168.219.130:8080/slowwalking/resources/images/"+sitterview.get("image").toString());
                    requestBoard.add(dto);
                }
                customAdapter  = new CustomAdapter(getContext(), requestBoard);
                customListView.setAdapter(customAdapter);
                customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(view.getContext(),
                                RequestDetail.class);
                        intent.putExtra("parents_id", requestBoard.get(position).getId());
                        intent.putExtra("idx", requestBoard.get(position).getIdx());
                        intent.putExtra("id", user_id);
                        intent.putExtra("flag",flag);
                        //액티비티 실행 //인덱스보내서 받아와서 이네덱스 참초해서

                        startActivity(intent);
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
