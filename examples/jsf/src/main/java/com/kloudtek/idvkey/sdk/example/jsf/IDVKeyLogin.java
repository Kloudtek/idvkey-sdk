/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk.example.jsf;

import com.kloudtek.idvkey.api.OperationResult;
import com.kloudtek.idvkey.sdk.IDVKeyAPIClient;
import com.kloudtek.util.JSFUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.Serializable;

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
    private String authOpId;

    public void login() throws IOException {
        final OperationResult operationResult = apiClient.authenticateUser(websiteId, JSFUtils.getContextURL("/rest/verifyauth"), );
        // since this bean is session scoped, this will be available later in the verifyAuth call below
        authOpId = operationResult.getOpId();
        JSFUtils.getExternalContext().redirect(operationResult.getRedirectUrl().toString());
    }

    @RequestMapping("/verifyauth")
    public ModelAndView verifyAuth(ModelMap model) throws IOException {
        // let's verify the user has authenticated successfully before setting him/her as authenticated.
        final String username = apiClient.confirmUserAuthentication(authOpId);
        userCtx.setUser(userDb.findUser(username));
        model.addAttribute("attribute", "redirectWithRedirectPrefix");
        return new ModelAndView("redirect:/loggedin.xhtml", model);
    }
}
