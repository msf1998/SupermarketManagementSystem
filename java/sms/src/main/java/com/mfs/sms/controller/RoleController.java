package com.mfs.sms.controller;

import com.mfs.sms.pojo.Result;
import com.mfs.sms.pojo.Role;
import com.mfs.sms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @RequestMapping("/add")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result addRole(@RequestBody Role role, HttpServletRequest request) {
        //System.out.println(role);
        try {
            return roleService.addRole(role,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/delete")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result deleteRole(@RequestBody Role role,HttpServletRequest request) {
        //System.out.println(role);
        try {
            return roleService.deleteRole(role,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/edit")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result editRole(@RequestBody Role role,HttpServletRequest request) {
        //System.out.println(role);
        try {
            return roleService.editRole(role,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/list")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result listRole(@RequestBody Role role, HttpServletRequest request) {
        //System.out.println(role);
        try {
            return roleService.listRole(role,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
}
