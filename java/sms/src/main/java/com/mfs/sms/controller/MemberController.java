package com.mfs.sms.controller;

import com.mfs.sms.pojo.Member;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.serviceImpl.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/number")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @RequestMapping("/add")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result addNumber(@RequestBody Member member, HttpServletRequest request) {
        //System.out.println(number);
        try {
            return memberService.addNumber(member,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/delete")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result deleteNumber(@RequestBody Member member, HttpServletRequest request) {
        //System.out.println(number);
        try {
            return memberService.deleteNumber(member,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/edit")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result editNumber(@RequestBody Member member, HttpServletRequest request) {
        //System.out.println(number);
        try {
            return memberService.editNumber(member,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/list")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result listNumber(@RequestBody Member member, HttpServletRequest request) {
        //System.out.println(number);
        try {
            return memberService.listNumber(member,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
}
