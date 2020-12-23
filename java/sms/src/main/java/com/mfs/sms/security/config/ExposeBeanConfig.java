package com.mfs.sms.security.config;

import com.mfs.sms.security.RedisLogoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * 暴漏配置SpringSecurity相关的Bean
 * */
@Configuration
public class ExposeBeanConfig {

    /**
     * 暴漏自定义的RedisLogoutHandler
     * */
    @Bean
    public LogoutHandler logoutHandler() {
        return RedisLogoutHandler.getInstance();
    }


}
