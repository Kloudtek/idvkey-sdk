/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yannick on 17/3/16.
 */
public class ServiceLink {
    @JsonProperty
    private String serviceId;
    @JsonProperty
    private String userRef;

    public ServiceLink() {
    }

    public ServiceLink(String serviceId, String userRef) {
        this.serviceId = serviceId;
        this.userRef = userRef;
    }

    public String getUserRef() {
        return userRef;
    }

    public void setUserRef(String userRef) {
        this.userRef = userRef;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
