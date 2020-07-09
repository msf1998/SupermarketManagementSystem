package com.mfs.sms.controller;

import com.mfs.sms.pojo.Result;
import com.mfs.sms.pojo.User;
import com.mfs.sms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/api/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/add")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result addProduct(@RequestBody User user, HttpServletRequest request) {
        //System.out.println(type);
        try {
            return userService.addUser(user,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/delete")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result deleteUser(@RequestBody User user,HttpServletRequest request) {
        //System.out.println(user);
        try {
            return userService.deleteUser(user,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/edit")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result editUser(@RequestBody User user,HttpServletRequest request) {
        //System.out.println(user);
        try {
            return userService.editUser(user,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/list")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result listUser(@RequestBody User user, HttpServletRequest request) {
        //System.out.println(user);
        try {
            return userService.listUser(user,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    @RequestMapping("/edit/me1")
    public Result editMe1(@RequestParam("head") MultipartFile head, @RequestParam("password") String password, HttpServletRequest request) {
        try {
            return userService.editMe(head,password,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/edit/me2")
    public Result editMe2(@RequestParam("password") String password, HttpServletRequest request) {
        try {
            return userService.editMe(null,password,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    @RequestMapping("/get")
    public Result getMe(HttpServletRequest request) {
        try {
            return userService.getMe(request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    @RequestMapping("/login")
    //@CrossOrigin(allowCredentials = "true",origins = {"*"})
    public Result login(@RequestBody User user, HttpServletResponse response) {
        try {
             return userService.login(user,response);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    @RequestMapping("/register")
    //@CrossOrigin(allowCredentials = "true",origins = {"*"})
    public Result register(@RequestBody User user) {
        try {
            return userService.register(user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    @RequestMapping("/checkExist")
    //@CrossOrigin(allowCredentials = "true",origins = {"*"})
    public Result checkExist(@RequestBody User user) {
        //System.out.println(user);
        try {
            return userService.checkExist(user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
}
