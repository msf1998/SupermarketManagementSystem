package com.mfs.sms.controller;

import com.mfs.sms.mapper.OrderMapper;
import com.mfs.sms.pojo.Order;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.pojo.to.SaleTo;
import com.mfs.sms.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;


    @RequestMapping("/finance")
    public Result getFinanceChart(HttpServletRequest request) {
        try {
            return orderService.getFinanceChart(request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/pay")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result createOrder(@RequestBody SaleTo saleTo, HttpServletRequest request) {
        //System.out.println(saleTo);
        try {
            return orderService.createOrder(saleTo,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/add")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result addOrder(@RequestBody Order order, HttpServletRequest request) {
        System.out.println(order);
        try {
            return orderService.addOrder(order,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/delete")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result deleteOrder(@RequestBody Order order,HttpServletRequest request) {
        //System.out.println(order);
        try {
            return orderService.deleteOrder(order,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/edit")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result editOrder(@RequestBody Order order,HttpServletRequest request) {
        //System.out.println(order);
        try {
            return orderService.editOrder(order,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/list")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result listOrder(@RequestBody Order order, HttpServletRequest request) {
        //System.out.println(order);
        try {
            return orderService.listOrder(order,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
}
