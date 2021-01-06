package com.mfs.sms.controller;

import com.mfs.sms.pojo.Result;
import com.mfs.sms.pojo.User;
import com.mfs.sms.serviceImpl.UserService;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.IOException;
import java.security.Principal;

@RequestMapping("/api/user")
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 添加用户
     * */
    @RequestMapping("/add")
    @ResponseBody
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result addProduct(User user,Principal principal) {
        //System.out.println(type);
        try {
            return userService.addUser(principal,user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 删除用户
     * */
    @RequestMapping("/delete")
    @ResponseBody
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result deleteUser(User user,Principal principal) {
        //System.out.println(user);
        try {
            return userService.deleteUser(principal,user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    /**
     * 修改用户信息
     * */
    @RequestMapping("/edit")
    @ResponseBody
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result editUser(User user,Principal principal) {
        System.out.println(user);
        try {
            return userService.editUser(principal,user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 获取用户列表（可分页）
     * */
    @RequestMapping("/list")
    @ResponseBody
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result listUser(Principal principal,User user) {
        //System.out.println(user);
        try {
            return userService.listUser(principal,user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 修改密码
     * */
    @RequestMapping("/edit/password")
    @ResponseBody
    public Result editPassword(@RequestParam("password") String password,Principal principal) {
        try {
            return userService.editPassword(principal, password);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 修改用户头像
     * */
    @RequestMapping("/edit/head")
    @ResponseBody
    public Result editHead(MultipartFile head, Principal principal) {
        try {
            return userService.editHead(principal,head);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 获取当前登录用户的详细信息
     * */
    @RequestMapping("/get")
    @ResponseBody
    public Result getMe(Principal principal) {
        try {
             return userService.getMe(principal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 注册
     * */
    @RequestMapping("/register")
    //@CrossOrigin(allowCredentials = "true",origins = {"*"})
    public String register(User user,Model model,HttpServletResponse response,HttpServletRequest request)  {
        Result result = null;
        try {
             result = userService.register(user);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(3,"服务器异常",null,null);
        }
        model.addAttribute(result);
        if (result.getStatus() == 1) {
            response.setHeader("refresh","2;url=" + request.getContextPath() + "/login");
        }
        return "register";
    }

    /**
     * 登录
     * */
    @RequestMapping("/login")
    public void cacheLogin(User user,HttpServletRequest request,HttpServletResponse response){
        Result result = null;
        try {
            result = userService.cacheLogin(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setHeader("refresh","0;URL=" + request.getContextPath() + "/index");
    }

    /**
     * 检查当前用户名是否已经注册
     * */
    @RequestMapping("/check")
    //@CrossOrigin(allowCredentials = "true",origins = {"*"})
    public Result checkExist(User user) {
        //System.out.println(user);
        try {
            return userService.checkExist(user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

}
