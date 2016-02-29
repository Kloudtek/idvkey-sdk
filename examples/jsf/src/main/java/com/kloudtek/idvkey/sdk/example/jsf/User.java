/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk.example.jsf;

import java.io.Serializable;

/**
 * Created by yannick on 1/8/16.
 */
public class User implements Serializable {
    private String username;
    private String password;
    private String idvkeyId;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean comparePassword(String password) {
        return this.password.equals(password);
    }

    public String getIdvkeyId() {
        return idvkeyId;
    }

    public void setIdvkeyId(String idvkeyId) {
        this.idvkeyId = idvkeyId;
    }
}
