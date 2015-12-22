package com.example.htp.model;

import android.databinding.BaseObservable;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by HTP on 2015/11/10.
 */
public class User extends BaseObservable{

    public User()
    {

    }

    public User(String name, String password, String sex) {
        this.name = name;
        this.password = password;
        this.sex = sex;
    }

    private String name;
    private String password;
    private String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


}

