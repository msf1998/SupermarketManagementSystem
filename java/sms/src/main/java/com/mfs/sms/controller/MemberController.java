package com.mfs.sms.controller;

import com.mfs.sms.pojo.Member;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.serviceImpl.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/api/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    /**
     * 添加会员
     * */
    @RequestMapping("/add")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result addMember(Member member, Principal principal) {
        //System.out.println(number);
        try {
            return memberService.addMember(principal,member);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 删除会员
     * */
    @RequestMapping("/delete")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result deletember(Member member, Principal principal) {
        //System.out.println(number);
        try {
            return memberService.deleteMember(principal,member);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 修改会员信息
     * */
    @RequestMapping("/edit")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result editMember(Principal principal,Member member) {
        //System.out.println(number);
        try {
            return memberService.editMember(principal,member);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 获取会员列表
     * */
    @RequestMapping("/list")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result listMember(Principal principal,Member member) {
        //System.out.println(number);
        try {
            return memberService.listMember(principal,member);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
}
