package com.mfs.sms.controller;

import com.mfs.sms.pojo.OrderDetail;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.serviceImpl.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/orderdetail")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;
    @RequestMapping("/list")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result listOrder(@RequestBody OrderDetail orderDetail, HttpServletRequest request) {
        //System.out.println(orderDetail);
        try {
            return orderDetailService.listOrderDetail(orderDetail,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
}
