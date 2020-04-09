package com.example.expense;

public class Spending {
    public String date, des, amt,text;
    public Spending(String d, String e,String a, String g)
    {
        date=d;
        des=e;
        amt=a;
        text=g;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

}
