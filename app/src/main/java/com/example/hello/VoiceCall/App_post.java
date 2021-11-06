package com.example.hello.VoiceCall;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class App_post {

    @SerializedName("aid")
    @Expose
    public String aid;
    @SerializedName("uid")
    @Expose
    public String uid;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("secret")
    @Expose
    public String secret;
    @SerializedName("u_users")
    @Expose
    public String uUsers;
    @SerializedName("u_groups")
    @Expose
    public String uGroups;
    @SerializedName("d_storage")
    @Expose
    public String dStorage;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("server")
    @Expose
    public String server;
    @SerializedName("notify")
    @Expose
    public String notify;
    @SerializedName("nrate")
    @Expose
    public String nrate;
    @SerializedName("ninterval")
    @Expose
    public String ninterval;
    @SerializedName("flag")
    @Expose
    public String flag;
    @SerializedName("f_beta")
    @Expose
    public String fBeta;
    @SerializedName("ts")
    @Expose
    public String ts;
    @SerializedName("uts")
    @Expose
    public String uts;
    @SerializedName("fcm_id")
    @Expose
    public String fcmId;
    @SerializedName("fcm_key")
    @Expose
    public String fcmKey;
    @SerializedName("apn_info")
    @Expose
    public String apnInfo;
    @SerializedName("pushflags")
    @Expose
    public String pushflags;
    @SerializedName("token")
    @Expose
    public String token;


    public void setAid(String aid) {
        this.aid = aid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setuUsers(String uUsers) {
        this.uUsers = uUsers;
    }

    public void setuGroups(String uGroups) {
        this.uGroups = uGroups;
    }

    public void setdStorage(String dStorage) {
        this.dStorage = dStorage;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public void setNrate(String nrate) {
        this.nrate = nrate;
    }

    public void setNinterval(String ninterval) {
        this.ninterval = ninterval;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setfBeta(String fBeta) {
        this.fBeta = fBeta;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public void setUts(String uts) {
        this.uts = uts;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }

    public void setFcmKey(String fcmKey) {
        this.fcmKey = fcmKey;
    }

    public void setApnInfo(String apnInfo) {
        this.apnInfo = apnInfo;
    }

    public void setPushflags(String pushflags) {
        this.pushflags = pushflags;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAid() {
        return aid;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getSecret() {
        return secret;
    }

    public String getuUsers() {
        return uUsers;
    }

    public String getuGroups() {
        return uGroups;
    }

    public String getdStorage() {
        return dStorage;
    }

    public String getUrl() {
        return url;
    }

    public String getServer() {
        return server;
    }

    public String getNotify() {
        return notify;
    }

    public String getNrate() {
        return nrate;
    }

    public String getNinterval() {
        return ninterval;
    }

    public String getFlag() {
        return flag;
    }

    public String getfBeta() {
        return fBeta;
    }

    public String getTs() {
        return ts;
    }

    public String getUts() {
        return uts;
    }

    public String getFcmId() {
        return fcmId;
    }

    public String getFcmKey() {
        return fcmKey;
    }

    public String getApnInfo() {
        return apnInfo;
    }

    public String getPushflags() {
        return pushflags;
    }

    public String getToken() {
        return token;
    }
}
