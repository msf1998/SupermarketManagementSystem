package com.mfs.sms.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * 拦截所有请求打印相关信息。调试使用。
 * 可以通过custom.log.enabled配置
 * */
@Component
public class ParameterFilter implements Filter {
    @Value("${custom.log.enabled}")
    private boolean enabled;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (enabled) {
            String log = new Date().toLocaleString() + " " + "CUSTOM" + "    " + "mfs" + "   --- " + this.getClass().getName() + "   :   ";
            HttpServletRequest re = (HttpServletRequest) request;
            Map<String, String[]> map = re.getParameterMap();
            log += re.getMethod() + " " + re.getRequestURI() + "    parameters={";
            for (Map.Entry<String, String[]> entry : map.entrySet()) {
                log += entry.getKey() + "=" + entry.getValue()[0] + ",";
            }
            log += "}";
            System.out.println(log);
        }
        chain.doFilter(request, response);
    }
}
