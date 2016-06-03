/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk.example.jsf;

import com.kloudtek.idvkey.api.AuthenticationRequestStatus;
import com.kloudtek.idvkey.api.OperationResult;
import com.kloudtek.idvkey.api.ServiceLinkRequestStatus;
import com.kloudtek.idvkey.api.ServiceNotFoundException;
import com.kloudtek.idvkey.sdk.IDVKeyAPIClient;
import com.kloudtek.idvkey.sdk.UserAlreadyLinkedException;
import com.kloudtek.idvkey.sdk.UserNotLinkedException;
import com.kloudtek.util.JSFUtils;
import com.kloudtek.util.URLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

/**
 * Created by yannick on 1/15/16.
 */
@Controller("idvkeyAuth")
@Scope("session")
public class IDVKeyLogin implements Serializable {
    @Autowired
    private transient IDVKeyAPIClient apiClient;
    @Autowired
    private transient UserCtx userCtx;
    @Autowired
    private transient UserDb userDb;
    @Value("${websiteId}")
    private transient String websiteId;
    private String preIdentifiedUser;
    private String authOpId;
    private String idvkeyId;

    public void login() throws IOException {
        URL callbackUrl = new URL(JSFUtils.getContextURL("/rest/verifyauth"));
        URL cancelUrl = new URL(JSFUtils.getContextURL("/login.xhtml"));
        final OperationResult operationResult;
        if (preIdentifiedUser != null) {
            try {
                operationResult = apiClient.authenticatePreIdentifiedUser(websiteId, callbackUrl, cancelUrl, null, preIdentifiedUser);
            } catch (UserNotLinkedException e) {
                // So we'll just remove the idvkeyid (userRef) from the database and login the user normally
                User user = userDb.findUserByIdvkeyId(preIdentifiedUser);
                user.setIdvkeyId(null);
                userCtx.setUser(user);
                JSFUtils.redirect(JSFUtils.getContextURL("/index.xhtml"));
                return;
            }
        } else {
            operationResult = apiClient.authenticateUser(websiteId, callbackUrl, cancelUrl, null);
        }
        // since this bean is session scoped, this will be available later in the verifyAuth call below
        authOpId = operationResult.getOpId();
        JSFUtils.redirect(operationResult.getRedirectUrl().toString());
    }

    @RequestMapping("/verifyauth")
    public ModelAndView verifyAuth(ModelMap model, HttpServletRequest request) throws IOException {
        // let's verify the user has authenticated successfully before setting him/her as authenticated.
        AuthenticationRequestStatus status = apiClient.getAuthenticationStatus(authOpId, preIdentifiedUser != null);
        idvkeyId = status.getUserRef();
        User user = userDb.findUserByIdvkeyId(idvkeyId);
        model.addAttribute("attribute", "redirectWithRedirectPrefix");
        if (user == null) {
            user = new User(idvkeyId, "password");
            user.setIdvkeyId(idvkeyId);
        }
        userCtx.setUser(user);
        return new ModelAndView("redirect:" + createRelativeUrl("/index.xhtml", request), model);
    }

    private String createRelativeUrl(String path, HttpServletRequest request) {
        return new URLBuilder(request.getContextPath()).path(path).toString();
    }

    public void link() throws ServiceNotFoundException, IOException {
        try {
            URL callbackUrl = new URL(JSFUtils.getContextURL("/rest/verifylink"));
            URL cancelUrl = new URL(JSFUtils.getContextURL("/index.xhtml"));
            final OperationResult operationResult = apiClient.linkUser(websiteId, callbackUrl, userCtx.getUser().getUsername(), cancelUrl);
            // since this bean is session scoped, this will be available later in the verifyAuth call below
            authOpId = operationResult.getOpId();
            JSFUtils.getExternalContext().redirect(operationResult.getRedirectUrl().toString());
        } catch (UserAlreadyLinkedException e) {
            userCtx.getUser().setIdvkeyId(userCtx.getUser().getUsername());
            JSFUtils.redirect("/index.xhtml");
        }
    }

    @RequestMapping("/verifylink")
    public ModelAndView verifyLink(ModelMap model, HttpServletRequest request) throws IOException {
        ServiceLinkRequestStatus status = apiClient.getServiceLinkRequestStatus(idvkeyId, authOpId);
        if (status == ServiceLinkRequestStatus.ACCEPTED) {
            userCtx.getUser().setIdvkeyId(userCtx.getUser().getUsername());
        }
        return new ModelAndView("redirect:" + createRelativeUrl("/index.xhtml", request), model);
    }

    public String getIdvkeyId() {
        return idvkeyId;
    }

    public void setPreIdentifiedUser(String preIdentifiedUser) {
        this.preIdentifiedUser = preIdentifiedUser;
    }
}
