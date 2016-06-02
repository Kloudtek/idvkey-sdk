/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk.example.jsf;

import com.kloudtek.idvkey.sdk.IDVKeyAPIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.security.InvalidKeyException;
import java.util.logging.Logger;

/**
 * Created by yannick on 1/14/16.
 */
@Component
@Scope("session")
public class UserCtx {
    private static final Logger logger = Logger.getLogger(UserCtx.class.getName());
    private User user;
    private String authOpId;
    @Autowired
    private transient IDVKeyAPIClient apiClient;
    @Value("${websiteId}")
    private transient String websiteId;
    private transient String linkedUserRef;

    public UserCtx() throws InvalidKeyException {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getWebsiteId() {
        return websiteId;
    }

    public String getLinkedUserRef() {
        return user.getIdvkeyId();
    }

    public boolean isLinked() {
        return user != null && user.getIdvkeyId() != null;
    }
}
