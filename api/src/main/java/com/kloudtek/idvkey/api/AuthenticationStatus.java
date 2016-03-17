/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yannick on 16/3/16.
 */
public class AuthenticationStatus extends NotificationStatus {
    @JsonProperty
    private boolean approved;
    @JsonProperty
    private String userRef;

    public AuthenticationStatus() {
    }

    public AuthenticationStatus(boolean replied, boolean accepted, String userRef) {
        super(replied);
        this.approved = accepted;
        this.userRef = userRef;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getUserRef() {
        return userRef;
    }

    public void setUserRef(String userRef) {
        this.userRef = userRef;
    }
}
