package com.example.hello.Modal_Class;

public class Friends {
    String uid,name,profileImg,phoneNo,lastMsg;
    Long lastMsgTime;

    public Friends() {
    }

    public Friends(String uid, String name, String profileImg, String phoneNo, String lastMsg, Long lastMsgTime) {
        this.uid = uid;
        this.name = name;
        this.profileImg = profileImg;
        this.phoneNo = phoneNo;
        this.lastMsg = lastMsg;
        this.lastMsgTime = lastMsgTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public Long getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(Long lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }
}
