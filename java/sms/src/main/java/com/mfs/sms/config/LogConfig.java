package com.mfs.sms.config;

import com.mfs.sms.utils.log.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfig {
    @Value("${custom.log.enabled}")
    private boolean enabled = false;
    @Value("${custom.log.config.location}")
    private String location = "console";
    @Bean
    public LogUtil registerLogUtil() {
        return new LogUtil(enabled,location);
    }
}
