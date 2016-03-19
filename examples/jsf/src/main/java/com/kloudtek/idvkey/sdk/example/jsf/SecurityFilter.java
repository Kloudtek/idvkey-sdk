/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk.example.jsf;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yannick on 18/3/16.
 */
@WebFilter(urlPatterns = "/*")
public class SecurityFilter implements Filter {
    private WebApplicationContext ctx;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ctx = WebApplicationContextUtils.findWebApplicationContext(filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = ((HttpServletRequest) request).getServletPath();
        if ((path.startsWith("/javax.faces.resource/") || path.startsWith("/images/") || path.startsWith("/public/")) || ctx.getBean(UserCtx.class).getUser() != null) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).sendRedirect("/index.xhtml");
        }
    }

    @Override
    public void destroy() {

    }
}
