package com.mfs.sms.aop;

import com.mfs.sms.mapper.LogMapper;
import com.mfs.sms.mapper.UserMapper;
import com.mfs.sms.pojo.Log;
import com.mfs.sms.pojo.User;
import com.mfs.sms.utils.CryptUtil;
import com.mfs.sms.utils.RequestUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

@Component
@Aspect
public class LogAspect {
    @Autowired
    private LogMapper logMapper;

    @Pointcut(value = "execution(* com.mfs.sms.service.*.*(..))")
    private void pointcut(){}

    /*前置通知
    @Before("pointcut()")
    public void before(JoinPoint joinPoint){
        System.out.println("this is before advice");
        Object[] args = joinPoint.getArgs();
        System.out.println(Arrays.toString(args));
    }*/
    /*后置通知
    @After("pointcut()")
    public void after(JoinPoint joinPoint) {
        System.out.println("this is after advice");
    }*/
    @AfterReturning(value = "pointcut()",returning = "object")
    public void afterReturning(JoinPoint joinPoint,Object object) {
        if (!(joinPoint.getSignature().getName().contains("checkExist") || joinPoint.getSignature().getName().contains("register"))) {
            Log log = Log.getLog();
            log.setId(null);
            log.setAction("{method:'"+ joinPoint.getSignature().getName() +"',args:'" + Arrays.toString(joinPoint.getArgs()) + "'}");
            log.setResult(object.toString().length() > 10240 ? object.toString().substring(0,10000) : object.toString());
            log.setCreateTime(new Date());
            String creator = null;
            if (joinPoint.getSignature().getName().contains("login")) {
                Object[] args = joinPoint.getArgs();
                User user = (User)args[0];
                creator = (user.getId() == null ? "未知" : user.getId());
            } else {
                Object[] args = joinPoint.getArgs();
                for (Object o : args) {
                    if (o.getClass().getName().contains("request")) {
                        HttpServletRequest request = (HttpServletRequest)o;
                        String userId = RequestUtil.getUserId(request);
                        creator = userId == null ? "未知" : userId;
                    }
                }
            }
            log.setCreator(creator);
            logMapper.add(log);
        }
    }
    //@AfterThrowing(value = "pointcut()",throwing = "e")
    public void afterThrowing(JoinPoint joinPoint,Throwable e) {
        if (!(joinPoint.getSignature().getName().contains("checkExist") || joinPoint.getSignature().getName().contains("register"))) {
            Log log = Log.getLog();
            log.setId(null);
            log.setAction("{method:'"+ joinPoint.getSignature().getName() +"',args:'" + Arrays.toString(joinPoint.getArgs()) + "'}");
            log.setResult(e.getMessage().length() > 1000 ? e.getMessage().substring(0,1000) : e.getMessage());
            log.setCreateTime(new Date());
            String creator = null;
            if (joinPoint.getSignature().getName().contains("login")) {
                Object[] args = joinPoint.getArgs();
                User user = (User)args[0];
                creator = (user.getId() == null ? "未知" : user.getId());
            } else {
                Object[] args = joinPoint.getArgs();
                for (Object o : args) {
                    if (o.getClass().getName().contains("request")) {
                        HttpServletRequest request = (HttpServletRequest)o;
                        String userId = RequestUtil.getUserId(request);
                        creator = userId == null ? "未知" : userId;
                    }
                }
            }
            log.setCreator(creator);
            logMapper.add(log);
        }
    }

    /*环绕通知
    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("this is around advice before");
        Object proceed = joinPoint.proceed();
        System.out.println("this is around advice before");
        return proceed;
    }*/
}
