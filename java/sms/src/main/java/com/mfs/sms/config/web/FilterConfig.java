package com.mfs.sms.config.web;

import com.mfs.sms.filter.ParameterFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    @Autowired
    public FilterRegistrationBean filterRegistration(ParameterFilter filter) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setOrder(Integer.MIN_VALUE + 100);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.setFilter(filter);
        return filterRegistrationBean;
    }
}
