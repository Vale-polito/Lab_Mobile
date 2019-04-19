package com.example.lab2firebase;

public class Readdatabase {
    private String restname,restaddress,restphone,restmobile;

    public Readdatabase() {
    }

    public Readdatabase(String restname, String restaddress, String restphone, String restmobile) {
        this.restname = restname;
        this.restaddress = restaddress;
        this.restphone = restphone;
        this.restmobile = restmobile;
    }

    public String getRestname() {
        return restname;
    }

    public void setRestname(String restname) {
        this.restname = restname;
    }

    public String getRestaddress() {
        return restaddress;
    }

    public void setRestaddress(String restaddress) {
        this.restaddress = restaddress;
    }

    public String getRestphone() {
        return restphone;
    }

    public void setRestphone(String restphone) {
        this.restphone = restphone;
    }

    public String getRestmobile() {
        return restmobile;
    }

    public void setRestmobile(String restmobile) {
        this.restmobile = restmobile;
    }
}
