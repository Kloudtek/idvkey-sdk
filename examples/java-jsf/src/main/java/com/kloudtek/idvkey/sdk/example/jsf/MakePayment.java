/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk.example.jsf;

import com.kloudtek.idvkey.api.*;
import com.kloudtek.idvkey.sdk.IDVKeyAPIClient;
import com.kloudtek.util.JSFUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by yannick on 4/2/16.
 */
@Component
@Scope("session")
public class MakePayment implements Serializable {
    private static final Logger logger = Logger.getLogger(MakePayment.class.getName());
    private static final long serialVersionUID = 1L;
    @Autowired
    private transient IDVKeyAPIClient apiClient;
    @Value("${websiteId}")
    private transient String websiteId;
    @Autowired
    private transient UserCtx userCtx;
    @Min(value = 1L, message = "Amount must be at least 1")
    private transient int amount;
    private transient String destination;
    private transient String opId;
    private HashMap<String, Payment> pendingOperations = new HashMap<String, Payment>();

    public void submit() throws IOException {
        String userRef = userCtx.getLinkedUserRef();
        URL callbackUrl = new URL(JSFUtils.getContextURL("/paymentsent.xhtml"));
        URL cancelUrl = new URL(JSFUtils.getContextURL("/paymentsent.xhtml?cancel=true"));
        String approvalTitle = "Approve payment";
        String approvalText = "Please approve payment of " + amount + "$ to " + destination;
        SecurityLevel securityLevel;
        if (amount <= 50) {
            securityLevel = SecurityLevel.LOW;
        } else if (amount <= 1000) {
            securityLevel = SecurityLevel.STANDARD;
        } else {
            securityLevel = SecurityLevel.HIGH;
        }
        ApprovalRequest approvalRequest = new ApprovalRequest(userRef, callbackUrl, approvalTitle, approvalText, securityLevel);
        approvalRequest.setShortText("Pay " + amount + "$ to " + destination);
        OperationResult operationResult = apiClient.requestApproval(websiteId, approvalRequest);
        opId = operationResult.getOpId();
        pendingOperations.put(opId, new Payment(destination, amount));
        JSFUtils.redirect(operationResult.getRedirectUrl().toString());
    }

    public void complete() throws IOException {
        ApprovalRequestStatus approvalState = apiClient.getApprovalStatus(opId);
        if (approvalState.getStatus() == ApprovalStatus.APPROVED) {
            Payment payment = pendingOperations.get(opId);
            if (payment == null) {
                throw new IllegalArgumentException("Invalid opId");
            }
            doPayment(payment);
            pendingOperations.remove(opId);
        } else {
            JSFUtils.redirect(JSFUtils.getContextURL("/index.xhtml"));
        }
    }

    private void doPayment(Payment payment) {
        logger.info("Sent payment of " + payment.getAmount() + " to " + payment.getTo());
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOpId() {
        return opId;
    }

    public void setOpId(String opId) {
        this.opId = opId;
    }
}
