package com.mfs.sms.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;

public final class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private JsonUtil(){}

    public static void writeObject(HttpServletResponse response, Object obj) throws Exception {
        response.setCharacterEncoding("utf8");
        response.setHeader("Content-Type","application/json");
        objectMapper.writeValue(response.getWriter(),obj);
    }
}
