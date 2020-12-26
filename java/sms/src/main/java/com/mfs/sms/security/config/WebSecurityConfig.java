package com.mfs.sms.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.sql.DataSource;

/**
 * Spring Security配置
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private LogoutHandler redisLogoutHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.debug(true);
        super.configure(web);
    }

    /**
     * 用户源配置
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username,password,not deleted from tb_user where username = ?")
                .rolePrefix("")
                .authoritiesByUsernameQuery("select username,role from tb_user where username = ?");
    }

    /**
     * 访问权限配置
     * 主要配置哪些路径是可以直接访问，哪些是必须登录才能访问
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.csrf().disable();
        //session管理配置
        http.sessionManagement()
                //会话超时转发
                .invalidSessionUrl("/login?invalid")
                //预防Session固定攻击，采用changeSessionId策略
                .sessionFixation().changeSessionId()
                //控制单个用户可用的Session数量，以及超过数量时所采取的措施。使用该功能前必须配置HttpSessionEventPublisher为Session监听器
                //url只有在非交互式登录方法中有效
                .sessionAuthenticationErrorUrl("/test").maximumSessions(1).maxSessionsPreventsLogin(false).expiredUrl("/force").and()

                //请求拦截配置
                .and().authorizeRequests()
                //静态资源放行
                .antMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/javascript/**", "/register","/invalid","/force").permitAll()
                //部分开放api放行
                .mvcMatchers("/api/user/register", "/api/user/check").permitAll()
                //拦截除上述放行之外的所有请求
                .anyRequest().authenticated()

                //认证方法配置，username/password+form表单登陆方式
                .and().formLogin()
                //转发登录请求以完成更多额外操作
                .successForwardUrl("/api/user/login")
                //指定登录页面和登陆成功跳转页面.defaultSuccessUrl("/index",false)
                .loginPage("/login").permitAll()

                //登出配置
                .and().logout().addLogoutHandler(redisLogoutHandler)
                //删除cookies
                .deleteCookies("JSESSIONID")
                .permitAll();
    }

}