/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk.example.jsf;

import com.kloudtek.util.JSFUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by yannick on 1/8/16.
 */
@Component
@Scope("request")
public class Login {
    @Autowired
    private UserDb userDb;
    @Autowired
    private IDVKeyLogin idvKeyLogin;
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

    public String login() throws IOException {
        final User user = userDb.findUser(username);
        if (user != null && user.comparePassword(password)) {
            if (user.getIdvkeyId() != null) {
                idvKeyLogin.setPreIdentifiedUser(user.getUsername());
                idvKeyLogin.login();
                return null;
            } else {
                return "/index";
            }
        } else {
            JSFUtils.addErrorMessage(null, "Invalid username / password");
            return null;
        }
    }

    public String logout() {
        JSFUtils.getHttpRequest().getSession().invalidate();
        return "/public/login";
    }
}
