/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

/**
 * Created by yannick on 17/3/16.
 */
public class ServiceLinkRequest {
    @JsonProperty
    private String userRef;
    @JsonProperty
    private URL url;
    @JsonProperty
    private URL cancelUrl;

    public ServiceLinkRequest() {
    }

    public ServiceLinkRequest(String userRef, URL url, URL cancelUrl) {
        this.userRef = userRef;
        this.url = url;
        this.cancelUrl = cancelUrl;
    }

    public String getUserRef() {
        return userRef;
    }

    public void setUserRef(String userRef) {
        this.userRef = userRef;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public URL getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(URL cancelUrl) {
        this.cancelUrl = cancelUrl;
    }
}
