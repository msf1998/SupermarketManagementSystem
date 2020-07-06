package com.mfs.sms.configration;

import com.mfs.sms.filter.LoginFilter;
import com.mfs.sms.filter.OriginalFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean OriginFilterConfig() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new OriginalFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("OriginFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean LoginFilterConfig() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new LoginFilter());
        filterRegistrationBean.addUrlPatterns("/api/*");
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.setName("LoginFilter");
        return filterRegistrationBean;
    }
}
