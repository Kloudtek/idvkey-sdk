/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.api;

import java.io.IOException;

/**
 * Created by yannick on 16/3/16.
 */
public class HttpException extends IOException {
    private static final long serialVersionUID = 1L;
    private final String reasonPhrase;
    private final int statusCode;

    public HttpException(String reasonPhrase, int statusCode, String body) {
        super("Server returned status code " + statusCode + " (" + reasonPhrase + ") :" + body);
        this.reasonPhrase = reasonPhrase;
        this.statusCode = statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
