package com.mfs.sms.security;

import com.mfs.sms.pojo.User;
import com.mfs.sms.utils.log.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 用户退出登录时清除redis中的已登录用户信息的缓存
 * */
public class RedisLogoutHandler implements LogoutHandler {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private LogUtil logUtil;
    private static final LogoutHandler instance = new RedisLogoutHandler();
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            Boolean deleted = redisTemplate.delete("login.user." + authentication.getName());
            //日志打印
            if (!deleted) {
                logUtil.log("清除Redis缓存：login." + authentication.getName() + "失败",this.getClass());
            } else {
                logUtil.log("清除Redis缓存：login." + authentication.getName() + "成功",this.getClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private RedisLogoutHandler(){}

    public static LogoutHandler getInstance(){
        return instance;
    }
}
