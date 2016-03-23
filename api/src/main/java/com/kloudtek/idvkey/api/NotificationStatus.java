/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yannick on 16/3/16.
 */
public class NotificationStatus {
    @JsonProperty(required = true)
    protected boolean replied;

    public NotificationStatus() {
    }

    public NotificationStatus(boolean replied) {
        this.replied = replied;
    }

    public boolean isReplied() {
        return replied;
    }

    public void setReplied(boolean replied) {
        this.replied = replied;
    }
}
