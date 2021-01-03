package com.mfs.sms.controller;

import com.mfs.sms.pojo.Result;
import com.mfs.sms.pojo.Role;
import com.mfs.sms.serviceImpl.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.print.PSPrinterJob;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 添加角色
     * */
    @RequestMapping("/add")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result addRole(Role role, Principal principal) {
        //System.out.println(role);
        try {
            return roleService.addRole(principal, role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 删除角色
     * */
    @RequestMapping("/delete")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result deleteRole(Role role, Principal principal) {
        //System.out.println(role);
        try {
            return roleService.deleteRole(principal, role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 修改角色
     * */
    @RequestMapping("/edit")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result editRole(Role role, Principal principal) {
        //System.out.println(role);
        try {
            return roleService.editRole(principal, role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 获取角色列表
     * */
    @RequestMapping("/list")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result listRole(Role role, Principal principal) {
        //System.out.println(role);
        try {
            return roleService.listRole(principal, role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
}
