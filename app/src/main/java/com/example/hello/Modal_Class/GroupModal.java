package com.example.hello.Modal_Class;

public class GroupModal {
    String tGroup;
    Group gInfo;

    public GroupModal(String tGroup, Group gInfo) {
        this.tGroup = tGroup;
        this.gInfo = gInfo;
    }

    public GroupModal() {
    }

    public String gettGroup() {
        return tGroup;
    }

    public void settGroup(String tGroup) {
        this.tGroup = tGroup;
    }

    public Group getgInfo() {
        return gInfo;
    }

    public void setgInfo(Group gInfo) {
        this.gInfo = gInfo;
    }
}
