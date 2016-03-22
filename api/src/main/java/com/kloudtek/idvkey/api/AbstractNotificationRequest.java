/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.net.URL;

/**
 * Created by yannick on 22/3/16.
 */
public class AbstractNotificationRequest {
    @JsonProperty(required = true)
    @NotEmpty
    private String serviceId;
    @JsonProperty(required = true)
    @NotNull
    private URL redirectUrl;
    @JsonProperty(required = true)
    @NotNull
    private URL cancelUrl;

    public AbstractNotificationRequest() {
    }

    public AbstractNotificationRequest(String serviceId, URL redirectUrl, URL cancelUrl) {
        this.serviceId = serviceId;
        this.redirectUrl = redirectUrl;
        this.cancelUrl = cancelUrl;
    }

    @NotNull
    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(@NotNull String serviceId) {
        this.serviceId = serviceId;
    }

    @NotNull
    public URL getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(@NotNull URL redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @NotNull
    public URL getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(@NotNull URL cancelUrl) {
        this.cancelUrl = cancelUrl;
    }
}
