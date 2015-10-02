/*
 * Copyright (c) 2015 Kloudtek Ltd
 */

package com.kloudtek.idvkey.api;

import java.net.URL;

/**
 * Operation result, which is returned by IDVKey when you request any kind of user authentication operation.
 */
public class OperationResult {
    private String opId;
    private URL redirectUrl;

    public OperationResult(String opId, URL redirectUrl) {
        this.opId = opId;
        this.redirectUrl = redirectUrl;
    }

    /**
     * Operation id
     *
     * @return operation id
     */
    public String getOpId() {
        return opId;
    }

    /**
     * URL that you should redirect your user to
     *
     * @return redirect url
     */
    public URL getRedirectUrl() {
        return redirectUrl;
    }
}
