package com.chalet.lskpi.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class WebLoginFilter implements Filter {
    
    Logger logger = Logger.getLogger(WebLoginFilter.class);

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        HttpSession session = request.getSession();
        String currentURL = request.getRequestURI();

        if( currentURL.contains("/showUploadData") ){
            if( null == session.getAttribute("web_login_user") ){
                String path = request.getContextPath();
                String basePath = request.getScheme() + "://" + request.getServerName() + ":"
                                + request.getServerPort() + path + "/";
                response.sendRedirect(basePath + "login");
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }

    public void init(FilterConfig arg0) throws ServletException {
        
    }
    
    public void destroy() {
        
    }

}
