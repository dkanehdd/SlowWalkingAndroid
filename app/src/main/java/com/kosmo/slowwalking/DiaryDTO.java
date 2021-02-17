package com.kosmo.slowwalking;

import android.os.Parcel;
import android.os.Parcelable;

public class DiaryDTO implements Parcelable {

    private String regidate;
    private String content;
    private String name;//시터 이름

    protected DiaryDTO(Parcel in) {
        regidate= in.readString();
        content= in.readString();
        name = in.readString();
    }

    public static Creator<DiaryDTO> CREATOR = new Creator<DiaryDTO>() {
        @Override
        public DiaryDTO createFromParcel(Parcel source) {
            return new DiaryDTO(source);
        }

        @Override
        public DiaryDTO[] newArray(int size) {
            return new DiaryDTO[size];
        }
    };

    public DiaryDTO() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(regidate);
        dest.writeString(content);
        dest.writeString(name);
    }

    public String getRegidate() {
        return regidate;
    }

    public void setRegidate(String regidate) {
        this.regidate = regidate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
