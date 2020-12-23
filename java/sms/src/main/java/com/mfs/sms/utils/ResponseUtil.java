package com.mfs.sms.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResponseUtil {
    /**
     * 设置请求重定向
     * @param response HttpServletResponse
     * @param location 重定向地址
     * */
    public static void redirect(HttpServletRequest request, HttpServletResponse response, String location) {
        response.setStatus(302);
        response.setHeader("location",request.getContextPath()+location);
    }
}
