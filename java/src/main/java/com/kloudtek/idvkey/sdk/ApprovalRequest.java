/*
 * Copyright (c) 2015 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

/**
 * Used to request a confirmation from a user
 */
public class ApprovalRequest {
    @JsonProperty
    protected String title;
    @JsonProperty
    protected String text;
    @JsonProperty
    protected boolean centeredHorizontal = true;
    @JsonProperty
    protected boolean centeredVertical = true;
    @JsonProperty
    protected String denyConfirmMsg;
    @JsonProperty
    protected String approveConfirmMsg;
    @JsonProperty
    protected String denyMsg;
    @JsonProperty
    protected String approveMsg;

    public ApprovalRequest() {
    }

    public ApprovalRequest(@NotNull String title, @NotNull String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

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
