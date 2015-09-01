/*
 * Copyright (c) 2015 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk;

import java.net.URL;

/**
 * Operation result, which is returned by IDVKey when you request any kind of user authentication operation.
 */
public class OperationResult {
    private Long opId;
    private URL redirectUrl;

    public OperationResult(Long opId, URL redirectUrl) {
        this.opId = opId;
        this.redirectUrl = redirectUrl;
    }

    /**
     * Operation id
     *
     * @return operation id
     */
    public Long getOpId() {
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
