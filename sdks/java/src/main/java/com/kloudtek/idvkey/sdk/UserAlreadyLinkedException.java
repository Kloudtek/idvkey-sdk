/*
 * Copyright (c) 2015 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk;

/**
 * Created by yannick on 10/3/15.
 */
public class UserAlreadyLinkedException extends Exception {
    public UserAlreadyLinkedException() {
    }

    public UserAlreadyLinkedException(String message) {
        super(message);
    }

    public UserAlreadyLinkedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyLinkedException(Throwable cause) {
        super(cause);
    }
}
