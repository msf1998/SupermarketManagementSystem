package com.mfs.sms.filter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebFilter(urlPatterns = "/*")
public class OriginalFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String origin = request.getHeader("origin");
        if (origin == null) {
            origin = request.getHeader("referer");
        }
        response.setHeader("Access-Control-Allow-Origin",origin);
        response.setHeader("Access-Control-Allow-Credentials","true");
        if(RequestMethod.OPTIONS.toString().equals(request.getMethod())) {
            String allowMethod = request.getHeader("Access-Control-Request-Method");
            String allowHeaders = request.getHeader("Access-Control-Request-Headers");
            response.setHeader("Access-Control-Max-Age", "86400");            // 浏览器缓存预检请求结果时间,单位:秒
            response.setHeader("Access-Control-Allow-Methods", allowMethod);  // 允许浏览器在预检请求成功之后发送的实际请求方法名
            response.setHeader("Access-Control-Allow-Headers", allowHeaders); // 允许浏览器发送的请求消息头
            return;
        }
        filterChain.doFilter(request,response);
    }
}
