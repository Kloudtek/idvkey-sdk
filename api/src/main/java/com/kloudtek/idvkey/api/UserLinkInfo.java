/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.api;

/**
 * Represents a user linked to a service.
 */
public class UserLinkInfo {
    private String serviceId;
    private String userRef;

    public UserLinkInfo() {
    }

    public UserLinkInfo(String customerServiceId, String userRef) {
        serviceId = customerServiceId;
        this.userRef = userRef;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getUserRef() {
        return userRef;
    }

    public void setUserRef(String userRef) {
        this.userRef = userRef;
    }
}
