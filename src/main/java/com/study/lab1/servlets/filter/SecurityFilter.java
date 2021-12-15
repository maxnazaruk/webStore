package com.study.lab1.servlets.filter;


import com.study.lab1.service.UserVerificationService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class SecurityFilter implements Filter {
    private List<String> userTokens;
    private UserVerificationService userVerificationService;

    public SecurityFilter(List<String> userTokens, UserVerificationService userVerificationService) {
        this.userTokens = userTokens;
        this.userVerificationService = userVerificationService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        if(httpServletRequest.getRequestURI().endsWith("/login")) {
            filterChain.doFilter(servletRequest, servletResponse);
        }else {
            if(userVerificationService.tokenVerification(httpServletRequest.getCookies(), userTokens)){
                filterChain.doFilter(servletRequest, servletResponse);
            }else {
                httpServletResponse.sendRedirect("/login");
            }
        }
    }

    @Override
    public void destroy() {

    }
}
