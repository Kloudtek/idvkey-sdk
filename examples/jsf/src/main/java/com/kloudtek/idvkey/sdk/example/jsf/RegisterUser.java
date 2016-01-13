/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk.example.jsf;

import com.kloudtek.util.JSFUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 * Created by yannick on 1/8/16.
 */
@ManagedBean
@RequestScoped
public class RegisterUser {
    @ManagedProperty("#{userDb}")
    private UserDb userDb;
    private String username;
    private String password;
    private String confirmPassword;

    public RegisterUser() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public UserDb getUserDb() {
        return userDb;
    }

    public void setUserDb(UserDb userDb) {
        this.userDb = userDb;
    }

    public String register() {
        if( ! password.equals(confirmPassword) ) {
            JSFUtils.addErrorMessage(null, "Passwords do not match");
            return null;
        } else {
            return null;
        }
    }
}
