package com.mfs.sms.controller;

import com.mfs.sms.pojo.OrderDetail;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.serviceImpl.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/api/orderdetail")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 根据条件获取订单相求列表
     * */
    @RequestMapping("/list")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result listOrder(OrderDetail orderDetail, Principal principal) {
        //System.out.println(orderDetail);
        try {
            return orderDetailService.listOrderDetail(principal,orderDetail);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
}
