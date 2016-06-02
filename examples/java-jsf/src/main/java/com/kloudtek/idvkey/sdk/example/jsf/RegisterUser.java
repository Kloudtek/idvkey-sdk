/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk.example.jsf;

import com.kloudtek.util.JSFUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yannick on 1/8/16.
 */
@Component
@Scope("request")
public class RegisterUser {
    @Autowired
    private UserDb userDb;
    @Autowired
    private UserCtx userCtx;
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

    public UserCtx getUserCtx() {
        return userCtx;
    }

    public void setUserCtx(UserCtx userCtx) {
        this.userCtx = userCtx;
    }

    public String register() {
        if( ! password.equals(confirmPassword) ) {
            JSFUtils.addErrorMessage(null, "Passwords do not match");
            return null;
        } else {
            final User user = userDb.createUser(username, password);
            userCtx.setUser(user);
            return "/index";
        }
    }
}
