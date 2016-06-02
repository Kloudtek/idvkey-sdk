package com.kloudtek.idvkey.sdk;

/**
 * Created by yannick on 2/6/16.
 */
public class UserNotLinkedException extends Exception {
    public UserNotLinkedException() {
    }

    public UserNotLinkedException(String message) {
        super(message);
    }

    public UserNotLinkedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotLinkedException(Throwable cause) {
        super(cause);
    }
}
