package com.mfs.sms.security.config;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
public class ListenerConfig {

    /**
     * 用于Spring Security监听Session事件，实现单用户可用session数量的控制
     * */
    @Bean
    public ServletListenerRegistrationBean registrationBean() {
        ServletListenerRegistrationBean sessionListener= new ServletListenerRegistrationBean();
        sessionListener.setListener(new HttpSessionEventPublisher());
        return sessionListener;
    }
}
