package com.mfs.sms.filter;

import com.mfs.sms.pojo.Result;
import com.mfs.sms.utils.CryptUtil;
import com.mfs.sms.utils.JsonUtil;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException{
        //System.out.println("进入");
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        if (request.getMethod().equals(RequestMethod.OPTIONS.toString()) || request.getRequestURI().contains("/api/user/login")
         || request.getRequestURI().contains("/api/user/register") || request.getRequestURI().contains("/api/user/checkExist")) {
            //System.out.println("OPTION");
            filterChain.doFilter(request,response);
        } else {
            //System.out.println("Not OPTION");
            Cookie[] cookies = request.getCookies();
            boolean f = false;
            if (cookies != null ) {
                for (Cookie c : cookies) {
                    //System.out.println(c.getName() + " " + c.getValue());
                    if (c.getName() != null && c.getName().equals("token")) {
                        String token = c.getValue();
                        if (token.contains("%22")) {
                            token = token.substring(3, token.length() - 3);
                        }
                        String[] split = CryptUtil.decryptByDES(token).split("##");
                        if (split.length == 2) {
                            Long date = Long.valueOf(split[1]);
                            if (new Date().getTime() - date <= 30 * 60 * 1000) {
                               f = true;
                               break;
                            }
                        }
                    }
                }
            }
            //System.out.println(f);
            if (f) {
                filterChain.doFilter(request,response);
            } else {
                try {
                    //System.out.println("Login Filter");
                    JsonUtil.writeObject(response,new Result(4,"请先登录",null,null));
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void destroy() {

    }
}
