package com.example.hello.Modal_Class;

import java.util.ArrayList;

public class UserStatus {
    String name , profileImg,uid;
    boolean auth = false;
    private Long lastupadted;
    private ArrayList<Status> statuses;

    public UserStatus() {
    }

    public UserStatus(String name, String profileImg, Long lastupadted, ArrayList<Status> statuses ,String uid) {
        this.name = name;
        this.uid = uid;
        this.profileImg = profileImg;
        this.lastupadted = lastupadted;
        this.statuses = statuses;
    }

    public UserStatus(String name, String profileImg, Long lastupadted) {
        this.name = name;
        this.profileImg = profileImg;
        this.lastupadted = lastupadted;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
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

    public Long getLastupadted() {
        return lastupadted;
    }

    public void setLastupadted(Long lastupadted) {
        this.lastupadted = lastupadted;
    }

    public ArrayList<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Status> statuses) {
        this.statuses = statuses;
    }
}
