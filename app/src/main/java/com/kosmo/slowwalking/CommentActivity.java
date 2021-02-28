package com.kosmo.slowwalking;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class CommentActivity extends AppCompatActivity {
    public static final String TAG = "iKosmo";
    ArrayList<Integer> diary_idx = new ArrayList<Integer>();
    ArrayList<String> Request_time = new ArrayList<String>() ;
    ArrayList<String> parentsName = new ArrayList<String>();
    ListView customListView;
    private static CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        final Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        customListView = (ListView)findViewById(android.R.id.list);
        String addr = getString(R.string.server_addr);
        new CommentList().execute(
                addr+"commentList",
                "id="+id
        );

    }



    public class CustomAdapter extends ArrayAdapter {

        private Context context;
        private List list;


        class ViewHolder {
            public TextView parentname;
            public TextView date;
            public TextView comment;
            public RatingBar star;
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
                convertView = layoutInflater.inflate(R.layout.commentview, parent, false);
            }

            viewHolder = new ViewHolder();
            viewHolder.parentname = (TextView) convertView.findViewById(R.id.send_id);
            viewHolder.date = (TextView) convertView.findViewById(R.id.writedate);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.comment);
            viewHolder.star = (RatingBar)convertView.findViewById(R.id.starrate);

            final DiaryDTO dto = (DiaryDTO) list.get(position);

            viewHolder.parentname.setText(dto.getSend_id());
            viewHolder.date.setText(dto.getRegidate());
            viewHolder.comment.setText(dto.getContent());
            viewHolder.star.setRating(dto.getStarrate());


            return convertView;
        }
    }

    //인터뷰 리스트 불러오기
    class CommentList extends AsyncTask<String, Void, String> {


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
                out.write(strings[1].getBytes());

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

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        //doInBackground()에서 반환값은 해당 메소드로 전달한다.
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            StringBuffer sb = new StringBuffer();
            try{
                /*

                 */
                //JSON객체를 파싱
                JSONArray jsonObject = new JSONArray(s);
                ArrayList<DiaryDTO> lists = new ArrayList<DiaryDTO>();
                for(int i=0 ; i<jsonObject.length() ; i++){//배열크기만큼반복
                    JSONObject diary = (JSONObject) jsonObject.get(i); //배열에서 하나씩 가져옴
                    DiaryDTO dto = new DiaryDTO();
                    dto.setRegidate(diary.get("regidate").toString());
                    dto.setContent(diary.get("content").toString());
                    dto.setSend_id(diary.get("send_id").toString());
                    dto.setStarrate(Integer.parseInt(diary.get("starrate").toString()));
                    lists.add(dto);
                }
                customAdapter = new CustomAdapter(CommentActivity.this, lists);
                customListView.setAdapter(customAdapter);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}