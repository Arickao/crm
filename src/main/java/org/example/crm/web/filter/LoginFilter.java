package org.example.crm.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class LoginFilter implements Filter {

    /**
     * 拦截恶意请求，重定向至登录页
     *
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String path = servletRequest.getServletPath();


    }
}
