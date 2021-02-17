package com.kosmo.slowwalking;

public class SitterListDTO {

    private String sitter_id;
    private String residence1;
    private String name;
    private String image_path;
    private int pay;
    private int starrate;
    private int age;

    public int getPay() {
        return pay;
    }

    public void setPay(Integer pay) {
        this.pay = pay;
    }

    public String getSitter_id() {
        return sitter_id;
    }

    public void setSitter_id(String sitter_id) {
        this.sitter_id = sitter_id;
    }


    public String getResidence1() {
        return residence1;
    }

    public void setResidence1(String residence1) {
        this.residence1 = residence1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public int getStarrate() {
        return starrate;
    }

    public void setStarrate(int starrate) {
        this.starrate = starrate;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
