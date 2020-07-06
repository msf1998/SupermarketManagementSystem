package com.mfs.sms.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public final class RequestUtil {
    private RequestUtil(){}

    public static String getUserId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null ) {
            for (Cookie c : cookies) {
                if (c.getName() != null && c.getName().equals("token")) {
                    String token = c.getValue();
                    if (token.contains("%22")) {
                        token = token.substring(3, token.length() - 3);
                    }
                    String[] split = CryptUtil.decryptByDES(token).split("##");
                    if (split.length == 2) {
                        return split[0];
                    } else {
                        return null;
                    }
                }
            }
            return null;
        } else {
            return null;
        }
    }

}
