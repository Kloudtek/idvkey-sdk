/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jetbrains.annotations.Nullable;

/**
 * Created by yannick on 16/3/16.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AuthenticationStatus extends NotificationStatus {
    private Boolean approved;
    @JsonProperty
    private String userRef;

    public AuthenticationStatus() {
    }

    public AuthenticationStatus(boolean replied, @Nullable Boolean accepted, @Nullable String userRef) {
        super(replied);
        this.approved = accepted;
        this.userRef = userRef;
    }

    @Nullable
    public Boolean isApproved() {
        return approved;
    }

    public void setApproved(@Nullable Boolean approved) {
        this.approved = approved;
    }

    @Nullable
    public String getUserRef() {
        return userRef;
    }

    public void setUserRef(String userRef) {
        this.userRef = userRef;
    }
}
