/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk.example.jsf;

import com.kloudtek.idvkey.sdk.IDVKeyAPIClient;
import com.kloudtek.idvkey.sdk.UserAlreadyLinkedException;
import com.kloudtek.util.JSFUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
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
    @Value("${unlinkLinked}")
    private transient boolean unlinkIfAlreadyLinked;

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
        return linkedUserRef;
    }

    public void setLinkedUserRef(String linkedUserRef) {
        this.linkedUserRef = linkedUserRef;
    }

    public boolean isLinked() throws IOException {
        return user != null && user.isIdvkeyLinked();
    }

    public void linkUser() throws IOException {
        final URL url;
        try {
            url = apiClient.linkUser(websiteId, JSFUtils.getContextURL("/linked.xhtml"), user.getUsername(), cancelUrl);
        } catch (UserAlreadyLinkedException e) {
            logger.warning("User " + user.getUsername() + " was already linked");
            if (unlinkIfAlreadyLinked) {
                // Note: You wouldn't normally do this in your own code, this is here just for debugging/testing purposes
                apiClient.unlinkUser(websiteId, user.getUsername());
                linkUser();
            } else {
                JSFUtils.getExternalContext().redirect("linked.xhtml?userRef=" + user.getUsername());
            }
            return;
        }
        JSFUtils.getExternalContext().redirect(url.toString());
    }

    public void verifyLink() {
        if (!linkedUserRef.equals(user.getUsername())) {
            throw new IllegalStateException("userRef does not match currently logged in user");
        }
        user.setIdvkeyLinked(true);
    }

}
