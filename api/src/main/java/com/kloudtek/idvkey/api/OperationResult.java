/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

/**
 * Operation result, which is returned by IDVKey when you request any kind of user authentication operation.
 */
public class OperationResult {
    @JsonProperty
    private String opId;
    @JsonProperty
    private String opRef;
    @JsonProperty
    private URL redirectUrl;

    public OperationResult() {
    }

    public OperationResult(String opId, String opRef, URL redirectUrl) {
        this.opId = opId;
        this.opRef = opRef;
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

    public void setOpId(String opId) {
        this.opId = opId;
    }

    /**
     * URL that you should redirect your user to
     *
     * @return redirect url
     */
    public URL getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(URL redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
