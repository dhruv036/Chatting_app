package com.example.hello.Modal_Class;

public class Status {
    private String  imgurl;
    private  Long timestamp;

    public Status() {
    }

    public Status(String imgurl, Long timestamp) {
        this.imgurl = imgurl;
        this.timestamp = timestamp;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
