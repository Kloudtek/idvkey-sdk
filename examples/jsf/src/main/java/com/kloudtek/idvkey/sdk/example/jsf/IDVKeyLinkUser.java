/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk.example.jsf;

import com.kloudtek.ktserializer.Serializable;
import com.kloudtek.util.JSFUtils;
import com.kloudtek.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yannick on 27/2/16.
 */
@Component("linkUser")
@Scope("session")
public class IDVKeyLinkUser implements Serializable {
    @Autowired
    private transient UserCtx userCtx;
    @Autowired
    private transient UserDb userDb;
    @Autowired
    private transient IDVKeyLogin idvKeyLogin;
    private String username;
    private String password;
    private String password2;

    public String linkExistingUser() {
        User user = userDb.findUser(username);
        if( user == null || ! user.comparePassword(password)  ) {
            JSFUtils.addErrorMessage(null,"Invalid Username/Password");
            return null;
        }
        user.setIdvkeyId(idvKeyLogin.getIdvkeyId());
        return "/loggedin";
    }

    public String linkNewUser() {
        if(StringUtils.isBlank(password) ) {
            JSFUtils.addErrorMessage(null,"Password is missing");
            return null;
        }
        if( !password.equals(password2) ) {
            JSFUtils.addErrorMessage(null,"Passwords do not match");
            return null;
        }
        User user = userDb.createUser(username, password);
        user.setIdvkeyId(idvKeyLogin.getIdvkeyId());
        userCtx.setUser(user);
        return "/index";
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

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
