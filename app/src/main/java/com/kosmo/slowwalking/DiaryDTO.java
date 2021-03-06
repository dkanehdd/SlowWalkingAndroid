package com.kosmo.slowwalking;

import android.os.Parcel;
import android.os.Parcelable;

public class DiaryDTO {

    private int its_idx;//다이어리의 일련번호
    private String regidate;
    private String send_id;
    private String rece_id;
    private String content;
    private String flag;
    private String name;//시터 이름
    private int idx;
    private int starrate;
    private String image_path;

    public int getIts_idx() {
        return its_idx;
    }

    public void setIts_idx(int its_idx) {
        this.its_idx = its_idx;
    }

    public String getRegidate() {
        return regidate;
    }

    public void setRegidate(String regidate) {
        this.regidate = regidate;
    }

    public String getSend_id() {
        return send_id;
    }

    public void setSend_id(String send_id) {
        this.send_id = send_id;
    }

    public String getRece_id() {
        return rece_id;
    }

    public void setRece_id(String rece_id) {
        this.rece_id = rece_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getStarrate() {
        return starrate;
    }

    public void setStarrate(int starrate) {
        this.starrate = starrate;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
