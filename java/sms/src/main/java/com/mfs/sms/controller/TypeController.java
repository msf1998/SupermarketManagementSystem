package com.mfs.sms.controller;

import com.mfs.sms.pojo.Result;
import com.mfs.sms.pojo.Type;
import com.mfs.sms.serviceImpl.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RequestMapping("/api/type")
@RestController
public class TypeController {
    @Autowired
    private TypeService typeService;

    /**
     * 增加类型
     * */
    @RequestMapping("/add")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result addType(Type type, Principal principal) {
        //System.out.println(type);
        try {
            return typeService.addType(principal,type);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 删除类型
     * */
    @RequestMapping("/delete")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result deleteType(Type type,Principal principal) {
        //System.out.println(type);
        try {
            return typeService.deleteType(principal,type);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 修改类型信息
     * */
    @RequestMapping("/edit")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result editType(Type type,Principal principal) {
        //System.out.println(type);
        try {
            return typeService.editType(principal,type);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 获取类型列表（可分页）
     * */
    @RequestMapping("/list")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result listType(Type type, Principal principal) {
        try {
            return typeService.listTypes(principal, type);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
}
