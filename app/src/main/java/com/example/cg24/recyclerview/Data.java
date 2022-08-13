package com.example.cg24.recyclerview;

public class Data {

    String point;
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public Data(String point, String date) {
        this.point = point;
        this.date = date;
    }

}
