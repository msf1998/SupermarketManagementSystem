package com.mfs.sms.filter;

import com.mfs.sms.utils.log.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
   private LogUtil logUtil;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            String log  = "";
            HttpServletRequest re = (HttpServletRequest) request;
            Map<String, String[]> map = re.getParameterMap();
            log += re.getMethod() + " " + re.getRequestURI() + "    parameters={";
            for (Map.Entry<String, String[]> entry : map.entrySet()) {
                log += entry.getKey() + "=" + entry.getValue()[0] + ",";
            }
            log += "}";
            logUtil.log(log,this.getClass());
        chain.doFilter(request, response);
    }
}
