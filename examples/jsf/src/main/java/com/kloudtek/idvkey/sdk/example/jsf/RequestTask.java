/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk.example.jsf;

import com.kloudtek.idvkey.api.ApprovalRequest;
import com.kloudtek.idvkey.api.ApprovalRequestStatus;
import com.kloudtek.idvkey.api.OperationResult;
import com.kloudtek.idvkey.sdk.IDVKeyAPIClient;
import com.kloudtek.util.JSFUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by yannick on 4/2/16.
 */
@Component
@Scope("session")
public class RequestTask implements Serializable {
    private static final long serialVersionUID = 1L;
    @Autowired
    private transient IDVKeyAPIClient apiClient;
    @Value("${websiteId}")
    private transient String websiteId;
    @Autowired
    private transient UserCtx userCtx;
    private transient String details;
    private transient String opId;
    private HashMap<String, String> pendingOperations = new HashMap<String, String>();

    public void submit() throws IOException {
        String userRef = userCtx.getLinkedUserRef();
        URL callbackUrl = new URL(JSFUtils.getContextURL("/dorequesttask.xhtml"));
        URL cancelUrl = new URL(JSFUtils.getContextURL("/dorequesttask.xhtml?cancel=true"));
        String approvalTitle = "Confirm operation";
        String approvalText = this.details;
        ApprovalRequest approvalRequest = new ApprovalRequest(websiteId, userRef, callbackUrl, cancelUrl, approvalTitle, approvalText);
        OperationResult operationResult = apiClient.requestApproval(approvalRequest);
        opId = operationResult.getOpId();
        pendingOperations.put(opId, approvalText);
        JSFUtils.redirect(operationResult.getRedirectUrl().toString());
    }

    public void complete() throws IOException {
        ApprovalRequestStatus approvalState = apiClient.getApprovalState(opId);
        if (approvalState.getState() == ApprovalRequestStatus.State.APPROVED) {
            details = pendingOperations.get(opId);
            if (details == null) {
                throw new IllegalArgumentException("Invalid opId");
            }
            pendingOperations.remove(opId);
        } else {
            JSFUtils.redirect(JSFUtils.getContextURL("/index.xhtml"));
        }
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getOpId() {
        return opId;
    }

    public void setOpId(String opId) {
        this.opId = opId;
    }
}
