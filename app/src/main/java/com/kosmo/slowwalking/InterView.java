package com.kosmo.slowwalking;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InterView extends LinearLayout {
    TextView textView1;
    TextView textView2;
    Button btnagree;
    //생성자
    public InterView(Context context) {
        super(context);
        //레이아웃의 전개를 위해 Inflater객체를 생성함
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
        //inflate()를 통해 레이아웃을 전개한다
        inflater.inflate(R.layout.interview_view, this, true);
        //데이터를 출력할 위젯을 얻어온다.
        textView1 = findViewById(R.id.parent_id);
        textView2 = findViewById(R.id.request_time);
        btnagree = findViewById(R.id.Btnagree);
    }
    //각 항목을 설정할 setter() 정의
    public void setName(String name) {
        textView1.setText(name);
    }
    public void setPhone(String Phone) {
        textView2.setText(Phone);
    }
    public void setImage(String text) {
        btnagree.setText(text);
    }
}
