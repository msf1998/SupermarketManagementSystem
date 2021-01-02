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
        System.out.println(saleTo);
        try {
            return orderService.createOrder(principal,saleTo);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 删除订单
     * */
    @RequestMapping("/delete")
    @ResponseBody
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result deleteOrder(Order order,Principal principal) {
        //System.out.println(order);
        try {
            return orderService.deleteOrder(principal, order);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 修改订单，仅能改备注
     * */
    @RequestMapping("/edit")
    @ResponseBody
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result editOrder(Principal principal, Order order) {
        //System.out.println(order);
        try {
            return orderService.editOrder(principal, order);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 获取订单列表（可分页）
     * */
    @RequestMapping("/list")
    @ResponseBody
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result listOrder(Principal principal,Order order) {
        //System.out.println(order);
        try {
            return orderService.listOrder(principal,order);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
}
