package com.example.hello;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.hello.Modal_Class.Friendinfo;
import com.example.hello.Modal_Class.Friends;
import com.example.hello.Modal_Class.Group;
import com.example.hello.Modal_Class.New_user;
import com.example.hello.Modal_Class.User;

import java.util.ArrayList;

public class FeatureController {

    String curr_user_phone;
    String name = "", receiverpho, receiveruid, userimg = "";
    User user = null;
    String uid = "";
    New_user new_user;
    int bool = 0;
    String isadmin = "0";
    String adminuid;
    Group group;
    String g_id = "";
    ArrayList<Friendinfo> groupFrdList;
    String my_mesibo_token = "";
    ArrayList<Friends> myfriends = new ArrayList<>();
    public static FeatureController instance = null;

    public static FeatureController getInstance() {
        if (instance == null) {
            instance = new FeatureController();
        }
        return instance;
    }

    public String getAdminuid() {
        return adminuid;
    }

    public void setAdminuid(String adminuid) {
        this.adminuid = adminuid;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getG_id() {
        return g_id;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }

    public New_user getNew_user() {

        return new_user;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setNew_user(New_user new_user) {
        this.new_user = new_user;
    }

    public static void setInstance(FeatureController instance) {
        FeatureController.instance = instance;
    }

    public String getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(String isadmin) {
        this.isadmin = isadmin;
    }

    public int getBool() {
        return bool;
    }

    public void setBool(int bool) {
        this.bool = bool;
    }

    public String getMy_mesibo_token() {
        return my_mesibo_token;
    }

    public void setMy_mesibo_token(String my_mesibo_token) {
        this.my_mesibo_token = my_mesibo_token;
    }

    public ArrayList<Friendinfo> getGroupFrdList() {
        return groupFrdList;
    }

    public void setGroupFrdList(ArrayList<Friendinfo> groupFrdList) {
        this.groupFrdList = groupFrdList;
    }

    public ArrayList<Friends> getMyfriends() {
        return myfriends;
    }

    public void setMyfriends(ArrayList<Friends> myfriends) {
        this.myfriends = myfriends;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public String getCurr_user_phone() {
        return curr_user_phone;
    }

    public void setCurr_user_phone(String curr_user_phone) {
        this.curr_user_phone = curr_user_phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReceiverpho() {
        return receiverpho;
    }

    public void setReceiverpho(String receiverpho) {
        this.receiverpho = receiverpho;
    }

    public String getReceiveruid() {
        return receiveruid;
    }

    public void setReceiveruid(String receiveruid) {
        this.receiveruid = receiveruid;
    }


    public void setTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }


}
