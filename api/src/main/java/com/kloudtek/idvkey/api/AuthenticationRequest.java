/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.api;

import java.net.URL;

/**
 * Created by yannick on 22/3/16.
 */
public class AuthenticationRequest extends AbstractNotificationRequest {
    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String serviceId, URL redirectUrl, URL cancelUrl) {
        super(serviceId, redirectUrl, cancelUrl);
    }
}
