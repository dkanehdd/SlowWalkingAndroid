package com.kosmo.slowwalking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kosmo.slowwalking.decorators.EventDecorator;
import com.kosmo.slowwalking.decorators.OneDayDecorator;
import com.kosmo.slowwalking.decorators.SaturdayDecorator;
import com.kosmo.slowwalking.decorators.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class calendarFragment extends Fragment {
    String time,kcal,menu;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    Cursor cursor;
    public static final String TAG = "ikosmo";
    MaterialCalendarView materialCalendarView;
    ArrayList<String> regidate;
    ArrayList<String> name;
    ArrayList<String> content;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "MenuFragement5 > onCreateView()");

        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.menu_fragment5, container, false);

        Bundle bundle = getArguments();
        regidate = bundle.getStringArrayList("regidate");
        name = bundle.getStringArrayList("name");
        content = bundle.getStringArrayList("content");
        materialCalendarView = viewGroup.findViewById(R.id.calendarView);
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator());

        new ApiSimulator(regidate).executeOnExecutor(Executors.newSingleThreadExecutor());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                String shot_Day = Year + "-" + Month + "-" + Day;
                boolean b = false;
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                for(int i=0; i<regidate.size() ; i++){
                    if(shot_Day.equals(regidate.get(i))){
                        b=true;
                        dlg.setTitle(name.get(i)+"님의 알림장입니다."); //제목
                        dlg.setMessage(content.get(i)); // 메시지
                        dlg.setIcon(R.drawable.chatballoon); // 아이콘 설정
//                버튼 클릭시 동작
                        dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                    }
                }
                Log.i("shot_Day test", shot_Day + "");
                if(b){
                    dlg.show();
                }
                else{
                    dlg.setTitle("등록된 알림장이 없습니다.");
                    dlg.setMessage(""); // 메시지
                    dlg.show();
                }
            }
        });
        return viewGroup;
    }
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        ArrayList<String> regidate2;

        ApiSimulator(ArrayList<String> regidate2){
            this.regidate2 = regidate2;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for(int i = 0 ; i < regidate2.size() ; i ++){
                CalendarDay day = CalendarDay.from(calendar);
                Log.i(TAG, regidate2.get(i));
                String[] time = regidate2.get(i).split("-");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                dates.add(day);
                calendar.set(year,month-1,dayy);
            }



            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays, (Activity) getContext()));
        }
    }
    
}

