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
public class Login {
    @ManagedProperty("#{userDb}")
    private UserDb userDb;
    private String username;
    private String password;

    public Login() {
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

    public UserDb getUserDb() {
        return userDb;
    }

    public void setUserDb(UserDb userDb) {
        this.userDb = userDb;
    }

    public String login() {
        final User user = userDb.findUser(username);
        if (user != null && user.comparePassword(password)) {
            return "loggedin.xhtml";
        } else {
            JSFUtils.addErrorMessage(null, "Invalid username / password");
            return null;
        }
    }
}
