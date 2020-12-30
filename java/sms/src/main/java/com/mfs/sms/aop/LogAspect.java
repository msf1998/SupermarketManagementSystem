package com.mfs.sms.aop;

import com.mfs.sms.mapper.UserMapper;
import com.mfs.sms.pojo.Log;
import com.mfs.sms.pojo.User;
import com.mfs.sms.utils.CryptUtil;
import com.mfs.sms.utils.RequestUtil;
import com.mfs.sms.utils.log.LogUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Arrays;
import java.util.Date;

@Component
@Aspect
public class LogAspect {
    @Autowired
    private LogUtil logUtil;

    @Pointcut(value = "execution(* com.mfs.sms.serviceImpl.*.*(..))")
    private void pointcut(){}

    /*前置通知
    @Before("pointcut()")
    public void before(JoinPoint joinPoint){
        System.out.println("this is before advice");
        Object[] args = joinPoint.getArgs();
        System.out.println(Arrays.toString(args));
    }*/
    //后置通知
    /*@After("pointcut()")
    public void after(JoinPoint joinPoint) {
        System.out.println("this is after advice");
        String kind = joinPoint.getKind();
        JoinPoint.StaticPart staticPart = joinPoint.getStaticPart();
    }*/
    @AfterReturning(value = "pointcut()",returning = "object")
    public void afterReturning(JoinPoint joinPoint, Object object) {
        if (!(joinPoint.getSignature().getName().contains("checkExist") || joinPoint.getSignature().getName().contains("register") || joinPoint.getSignature().getName().contains("delegatingCheckUser"))) {
            Log log = new Log();
            log.setAction("{method:'"+ joinPoint.getSignature().getName() +"',args:'" + Arrays.toString(joinPoint.getArgs()) + "'}");
            log.setResult(object == null ? null : object.toString());
            String creator = null;
            if (joinPoint.getSignature().getName().contains("login")) {
                Object[] args = joinPoint.getArgs();
                User user = (User)args[0];
                creator = (user.getUsername() == null ? "未知" : user.getUsername());
            } else {
                Object[] args = joinPoint.getArgs();
                for (Object o : args) {
                    if (o.getClass().getName().contains("principal")) {
                        Principal principal = (Principal)o;
                        String userId = principal.getName();
                        creator = userId == null ? "未知" : userId;
                    }
                }
            }
            log.setCreator(creator);
            logUtil.log(log.toString(),this.getClass());
        }
    }
    @AfterThrowing(value = "pointcut()",throwing = "e")
    public void afterThrowing(JoinPoint joinPoint,Throwable e) {
        if (!(joinPoint.getSignature().getName().contains("checkExist") || joinPoint.getSignature().getName().contains("register"))) {
            Log log = new Log();
            log.setAction("{method:'"+ joinPoint.getSignature().getName() +"',args:'" + Arrays.toString(joinPoint.getArgs()) + "'}");
            log.setResult(e.getMessage());
            String creator = null;
            if (joinPoint.getSignature().getName().contains("login")) {
                Object[] args = joinPoint.getArgs();
                User user = (User)args[0];
                creator = (user.getUsername() == null ? "未知" : user.getUsername());
            } else {
                Object[] args = joinPoint.getArgs();
                for (Object o : args) {
                    if (o.getClass().getName().contains("principal")) {
                        Principal principal = (Principal)o;
                        String userId = principal.getName();
                        creator = userId == null ? "未知" : userId;
                    }
                }
            }
            log.setCreator(creator);
            logUtil.log(log,this.getClass());
        }
    }

    //环绕通知
    /*@Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("this is around advice before");
        Object proceed = joinPoint.proceed();
        System.out.println("this is around advice before");
        return proceed;
    }*/
}
