package com.kosmo.slowwalking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MenuFragment2 extends ListFragment {


    public static final String TAG = "ikosmo";
    ArrayList<String> interviewID;
    ArrayList<String> request_time;
    ArrayList<Integer> interview_idx;
    String flag, id;
    ListView listView;
    ArrayList<InterviewDTO> interviewlist;
    ViewGroup viewGroup;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        flag= bundle.getString("flag");
        id = bundle.getString("id");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "MenuFragement2 > onCreateView()");


        viewGroup = (ViewGroup)inflater.inflate(R.layout.menu_fragment2, container, false);
        listView = (ListView)viewGroup.findViewById(android.R.id.list);
        String addr = getResources().getString(R.string.server_addr);
        new InterviewAsyncHttpRequest().execute( //1. 수락안된 인터뷰리스트 불러오기
                addr+"interList",
                "id="+id,
                "flag="+flag
        );

        return viewGroup;
    }

    public class MyAdapter extends ArrayAdapter {

        private Context context;
        private List list;


        class ViewHolder {
            public LinearLayout interviewItem;
            public TextView textView1;
            public TextView textView2;
            public  Button btnchat;
            public Button btnagree;
            public Button btndelete;
        }

        public MyAdapter(Context context, ArrayList list){
            super(context, 0, list);
            this.context = context;
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null){
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                convertView = layoutInflater.inflate(R.layout.interview_view, parent, false);
            }

            viewHolder = new MyAdapter.ViewHolder();
            viewHolder.interviewItem = convertView.findViewById(R.id.interviewItems);
            viewHolder.textView1 = (TextView) convertView.findViewById(R.id.parent_id);
            viewHolder.textView2 = (TextView) convertView.findViewById(R.id.request_time);
            viewHolder.btnchat = (Button) convertView.findViewById(R.id.btnchat);
            viewHolder.btnagree = (Button) convertView.findViewById(R.id.Btnagree);
            viewHolder.btndelete = (Button) convertView.findViewById(R.id.Btndelete);

            final InterviewDTO dto = (InterviewDTO) list.get(position);
            if(flag.equals("sitter")){
                viewHolder.textView1.setText(dto.getParents_name());
            }
            else{
                viewHolder.textView1.setText(dto.getSitter_name());
            }
            viewHolder.textView2.setText(dto.getRequest_time());
            viewHolder.btnchat.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),
                            ChatActivity.class);
                    if(flag.equals("sitter")){
                        intent.putExtra("send_id", dto.getSitter_id());
                        intent.putExtra("rece_id", dto.getParents_id());
                    }
                    else{
                        intent.putExtra("send_id", dto.getParents_id());
                        intent.putExtra("rece_id", dto.getSitter_id());
                    }
                    intent.putExtra("room", dto.getIdx());
                    //액티비티 실행
                    startActivity(intent);
                }
            });
            viewHolder.btnagree.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String addr = getResources().getString(R.string.server_addr);
                    new InterviewAgree().execute(
                            addr+"agreeAction",
                            "idx=" + dto.getIdx(),
                            "flag=" + flag
                    );
                    viewHolder.btnagree.setText("대기");
                }
            });
            viewHolder.btndelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    showDialog(dto.getIdx(), flag);
                }
            });


            return convertView;
        }
    }
    //인터뷰 리스트 불러오기
    class InterviewAsyncHttpRequest extends AsyncTask<String, Void, String> {


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
                out.write("&".getBytes());
                out.write(strings[2].getBytes());

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
                {"lists":[{"idx":8,"parents_id":"dkanehdd","sitter_id":"dkanehd","request_time":"15:00 ~ 20:00",
                "parents_agree":"T","sitter_agree":"T","request_idx":10,"parents_name":null,"sitter_name":null},
                {"idx":7,"parents_id":"kosmo3","sitter_id":"dkanehd","request_time":"18:00 ~ 21:00","parents_agree":"T",
                "sitter_agree":"T","request_idx":9,"parents_name":null,"sitter_name":null}]}
                 */
                //JSON객체를 파싱
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = (JSONArray)jsonObject.get("lists");//lists로 배열을 먼저 얻어옴 []
                interviewlist = new ArrayList<InterviewDTO>();
                for(int i=0 ; i<jsonArray.length() ; i++){//배열크기만큼반복
                    JSONObject interview = (JSONObject) jsonArray.get(i); //배열에서 하나씩 가져옴
                    Log.i(TAG, interview.get("parents_id").toString()+" "+interview.get("request_time").toString());//디버깅용
                    InterviewDTO dto = new InterviewDTO();
                    dto.setRequest_idx(Integer.parseInt(interview.get("request_idx").toString()));
                    dto.setSitter_id(interview.get("sitter_id").toString());
                    dto.setParents_id(interview.get("parents_id").toString());
                    dto.setRequest_time(interview.get("request_time").toString());
                    dto.setIdx(Integer.parseInt(interview.get("idx").toString()));
                    if(flag.equals("sitter")){
                        dto.setParents_name(interview.get("parents_name").toString());//가져와서 컬렉션에 저장
                    }
                    else{
                        dto.setSitter_name(interview.get("sitter_name").toString());
                    }
                    interviewlist.add(dto);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            MyAdapter myAdapter = new MyAdapter(getContext(), interviewlist);
            listView.setAdapter(myAdapter);
            if(interviewlist==null||interviewlist.size()==0){
                viewGroup.findViewById(R.id.interviewX).setVisibility(TextView.VISIBLE);
            }
        }
    }

    class InterviewAgree extends AsyncTask<String, Void, String> {

        public static final String TAG = "ikosmo";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            StringBuffer receiveData = new StringBuffer();
            try {
                URL url = new URL(strings[0]);//파라미터1 : 요청 URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                out.write(strings[1].getBytes());
                out.write("&".getBytes());
                out.write(strings[2].getBytes());

                out.flush();
                out.close();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.i(TAG, "HTTP OK 성공");

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    String responseData;
                    while ((responseData = reader.readLine()) != null) {
                        receiveData.append(responseData + "\n\r");
                    }
                    reader.close();
                } else {
                    Log.i(TAG, connection.getResponseCode() + "");
                    Log.i(TAG, "HTTP OK 안됨");
                }
            } catch (Exception e) {
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
            try {
                //JSON객체를 파싱
                JSONObject jsonObject = new JSONObject(s);
                String message = jsonObject.get("message").toString();
                String mode = jsonObject.get("mode").toString();
                Toast.makeText(getContext(),
                        message,
                        Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    void showDialog(int idx, String flag) {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(getContext())
                .setTitle("삭제")
                .setMessage("정말 삭제하시겠습니까?")
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String addr = getResources().getString(R.string.server_addr);
                        new InterviewAgree().execute(
                                addr+"deleteAction",
                                "idx=" + idx,
                                "flag=" + flag
                        );
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

}

