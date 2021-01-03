package com.mfs.sms.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //registry.addViewController("/").setViewName("index");
        //registry.addViewController("/index").setViewName("index");
        registry.addViewController("/test").setViewName("test");
        registry.addViewController("/force").setViewName("forceLogout");
        registry.addViewController("/invalid").setViewName("invalidSession");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/monitor").setViewName("monitor");
        registry.addViewController("/product").setViewName("product");
        registry.addViewController("/type").setViewName("type");
        registry.addViewController("/member").setViewName("member");
        registry.addViewController("/order").setViewName("order");
        registry.addViewController("/user").setViewName("user");
        registry.addViewController("/role").setViewName("role");
    }
}
