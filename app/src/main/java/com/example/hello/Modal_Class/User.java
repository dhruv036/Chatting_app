package com.example.hello.Modal_Class;

public class User {
  String uid,name,profileImg,phoneNo,pass,email;
  
  public User()
  {

  }

    public User(String uid, String name, String profileImg, String phoneNo, String pass, String email) {
        this.uid = uid;
        this.name = name;
        this.profileImg = profileImg;
        this.phoneNo = phoneNo;
        this.pass = pass;
        this.email = email;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
