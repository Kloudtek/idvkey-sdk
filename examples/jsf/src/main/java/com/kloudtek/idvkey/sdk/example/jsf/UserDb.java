/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk.example.jsf;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.HashMap;

/**
 * Created by yannick on 1/8/16.
 */
@ManagedBean
@ApplicationScoped
public class UserDb {
    private HashMap<String, User> users = new HashMap<String, User>();

    public synchronized User findUser(String username) {
        return users.get(username);
    }

    public synchronized User createUser(String username, String password) {
        User user = new User(username, password);
        users.put(username, user);
        return user;
    }
}
