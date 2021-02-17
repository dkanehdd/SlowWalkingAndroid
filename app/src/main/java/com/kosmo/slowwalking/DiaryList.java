package com.kosmo.slowwalking;

import android.app.Person;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class DiaryList implements Parcelable {
    ArrayList<DiaryDTO> diarylist;
    public DiaryList(){ }
    protected DiaryList(Parcel in) {
        diarylist = new ArrayList<>();
        in.readTypedList(diarylist, DiaryDTO.CREATOR);
    }
    public static final Creator<DiaryList> CREATOR = new Creator<DiaryList>() {
        @Override public DiaryList createFromParcel(Parcel in) {
            return new DiaryList(in);
        }

        @Override public DiaryList[] newArray(int size) {
            return new DiaryList[size];
        }
    };
    @Override public int describeContents() {
        return 0;
    }
    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(diarylist);
    }
    public ArrayList<DiaryDTO> getDiarylist() {
        return diarylist;
    }
    public void setDiarylist(ArrayList<DiaryDTO> diarylist) {
        this.diarylist = diarylist;
    }
}

