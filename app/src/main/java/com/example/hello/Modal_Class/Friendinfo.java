package com.example.hello.Modal_Class;

public class Friendinfo {
    String frdphn, frdname, frduid;

    public Friendinfo(String frdphn, String frdname, String frduid) {
        this.frdphn = frdphn;
        this.frdname = frdname;
        this.frduid = frduid;
    }

    public Friendinfo() {
    }

    public String getFrdphn() {
        return frdphn;
    }

    public void setFrdphn(String frdphn) {
        this.frdphn = frdphn;
    }

    public String getFrdname() {
        return frdname;
    }

    public void setFrdname(String frdname) {
        this.frdname = frdname;
    }

    public String getFrduid() {
        return frduid;
    }

    public void setFrduid(String frduid) {
        this.frduid = frduid;
    }
}
