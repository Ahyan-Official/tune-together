package com.app.tunetogether.az;

public class Items {

    //model class for event
    private String pic;
    private String userid;
    private String title;
    private String des;
    private String dimension;
    private String date;
    private String id;
    private String status;
    private String useridRequest;
    String day,month,year;


    //constractor
    public Items(String pic, String userid, String title, String transport, String dimension, String date, String status) {
        this.pic = pic;
        this.userid = userid;
        this.title = title;
        this.des = transport;
        this.dimension = dimension;
        this.date = date;
        this.status = status;
    }
    //getter and setters

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUseridRequest() {
        return useridRequest;
    }

    public void setUseridRequest(String useridRequest) {
        this.useridRequest = useridRequest;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Items() {
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
