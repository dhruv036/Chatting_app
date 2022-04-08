package com.example.hello.Modal_Class;

import com.example.hello.Fragment.GroupChatFragment;

import java.util.ArrayList;

public class Group {
    String gIsadmin, gid, gName, gDiscpt, gIcon;
    ArrayList<Friendinfo> gMembers;
    String gLastmsg,glstMsgTime;
    String adminuid;
    public Group() {
    }

    public Group(String gIsadmin, String gid, String gName, String gDiscpt, String gIcon, ArrayList<Friendinfo> gMembers, String gLastmsg, String glstMsgTime) {
        this.gIsadmin = gIsadmin;
        this.gid = gid;
        this.gName = gName;
        this.gDiscpt = gDiscpt;
        this.gIcon = gIcon;
        this.gMembers = gMembers;
        this.gLastmsg = gLastmsg;
        this.glstMsgTime = glstMsgTime;
    }

    public String getgLastmsg() {
        return gLastmsg;
    }

    public void setgLastmsg(String gLastmsg) {
        this.gLastmsg = gLastmsg;
    }

    public String getAdminuid() {
        return adminuid;
    }

    public void setAdminuid(String adminuid) {
        this.adminuid = adminuid;
    }

    public String getGlstMsgTime() {
        return glstMsgTime;
    }

    public void setGlstMsgTime(String glstMsgTime) {
        this.glstMsgTime = glstMsgTime;
    }

    public String getgIsadmin() {
        return gIsadmin;
    }

    public void setgIsadmin(String gIsadmin) {
        this.gIsadmin = gIsadmin;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public String getgDiscpt() {
        return gDiscpt;
    }

    public void setgDiscpt(String gDiscpt) {
        this.gDiscpt = gDiscpt;
    }

    public String getgIcon() {
        return gIcon;
    }

    public void setgIcon(String gIcon) {
        this.gIcon = gIcon;
    }

    public ArrayList<Friendinfo> getgMembers() {
        return gMembers;
    }

    public void setgMembers(ArrayList<Friendinfo> gMembers) {
        this.gMembers = gMembers;
    }
}
