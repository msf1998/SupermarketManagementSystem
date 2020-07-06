package com.mfs.sms.controller;

import com.mfs.sms.pojo.Product;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.pojo.Type;
import com.mfs.sms.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/type")
@RestController
public class TypeController {
    @Autowired
    private TypeService typeService;

    @RequestMapping("/add")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result addType(@RequestBody Type type, HttpServletRequest request) {
        //System.out.println(type);
        try {
            return typeService.addType(type,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/delete")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result deleteType(@RequestBody Type type,HttpServletRequest request) {
        //System.out.println(type);
        try {
            return typeService.deleteType(type,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/edit")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result editType(@RequestBody Type type,HttpServletRequest request) {
        //System.out.println(type);
        try {
            return typeService.editType(type,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/list")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result listType(@RequestBody Type type, HttpServletRequest request) {
        //System.out.println(type);
        try {
            return typeService.listTypes(type,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
}
