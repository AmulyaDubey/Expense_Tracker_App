package com.example.expense;

public class Expenditure {
    public String date, des, amt, option, type;
    int stamp;

    public Expenditure(String d, String e, String a, String text, int st, String t) {
        date = d;
        des = e;
        amt = a;
        option = text;
        stamp = st;
        type=t;
    }


    public Expenditure() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public int getStamp() {
        return stamp;
    }

    public void setStamp(int stamp) {
        this.stamp = stamp;

    }
}
