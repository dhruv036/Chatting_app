package com.example.hello.VoiceCall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("app")
    @Expose
    public App_post app;
    @SerializedName("uts")
    @Expose
    public String uts;
    @SerializedName("disabled")
    @Expose
    public Integer disabled;
    @SerializedName("ipaddr")
    @Expose
    public String ipaddr;
    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("op")
    @Expose
    public String op;
    @SerializedName("result")
    @Expose
    public Boolean result;

    public App_post getApp() {
        return app;
    }

    public void setApp(App_post app) {
        this.app = app;
    }

    public String getUts() {
        return uts;
    }

    public void setUts(String uts) {
        this.uts = uts;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
