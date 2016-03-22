/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

/**
 * Used to request a confirmation from a user
 */
public class ApprovalRequest extends AbstractNotificationRequest {
    @JsonProperty(required = true)
    @javax.validation.constraints.NotNull
    private String userRef;
    @JsonProperty(required = true)
    @javax.validation.constraints.NotNull
    private String title;
    @JsonProperty(required = true)
    @javax.validation.constraints.NotNull
    private String text;
    @JsonProperty
    private boolean centeredHorizontal = true;
    @JsonProperty
    private boolean centeredVertical = true;
    @JsonProperty
    private String denyConfirmMsg;
    @JsonProperty
    private String approveConfirmMsg;
    @JsonProperty
    private String denyMsg;
    @JsonProperty
    private String approveMsg;

    public ApprovalRequest() {
    }

    public ApprovalRequest(@NotNull String serviceId, @NotNull String userRef, @NotNull URL redirectUrl, @NotNull URL cancelUrl, @NotNull String title, @NotNull String text) {
        super(serviceId, redirectUrl, cancelUrl);
        this.userRef = userRef;
        this.title = title;
        this.text = text;
    }

    @NotNull
    public String getUserRef() {
        return userRef;
    }

    public void setUserRef(@NotNull String userRef) {
        this.userRef = userRef;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    @NotNull
    public String getText() {
        return text;
    }

    public void setText(@NotNull String text) {
        this.text = text;
    }

    public boolean isCenteredHorizontal() {
        return centeredHorizontal;
    }

    public void setCenteredHorizontal(boolean centeredHorizontal) {
        this.centeredHorizontal = centeredHorizontal;
    }

    public boolean isCenteredVertical() {
        return centeredVertical;
    }

    public void setCenteredVertical(boolean centeredVertical) {
        this.centeredVertical = centeredVertical;
    }

    public String getDenyConfirmMsg() {
        return denyConfirmMsg;
    }

    public void setDenyConfirmMsg(String denyConfirmMsg) {
        this.denyConfirmMsg = denyConfirmMsg;
    }

    public String getApproveConfirmMsg() {
        return approveConfirmMsg;
    }

    public void setApproveConfirmMsg(String approveConfirmMsg) {
        this.approveConfirmMsg = approveConfirmMsg;
    }

    public String getDenyMsg() {
        return denyMsg;
    }

    public void setDenyMsg(String denyMsg) {
        this.denyMsg = denyMsg;
    }

    public String getApproveMsg() {
        return approveMsg;
    }

    public void setApproveMsg(String approveMsg) {
        this.approveMsg = approveMsg;
    }
}
