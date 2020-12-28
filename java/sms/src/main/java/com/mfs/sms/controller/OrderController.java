package com.mfs.sms.controller;

import com.mfs.sms.pojo.Order;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.pojo.to.SaleTo;
import com.mfs.sms.serviceImpl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
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
    /**
     * 创建订单完成支付
     * */
    @RequestMapping("/pay")
    @ResponseBody
    public Result createOrder(Principal principal,SaleTo saleTo) {
        //System.out.println(saleTo);
        try {
            return orderService.createOrder(principal,saleTo);
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
