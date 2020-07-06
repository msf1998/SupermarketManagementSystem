package com.mfs.sms.service;

import com.mfs.sms.mapper.OrderDetailMapper;
import com.mfs.sms.mapper.OrderMapper;
import com.mfs.sms.mapper.UserMapper;
import com.mfs.sms.pojo.*;
import com.mfs.sms.pojo.Number;
import com.mfs.sms.utils.CryptUtil;
import com.mfs.sms.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 5)
    public Result addOrder(Order order, HttpServletRequest request){
        //用户验证
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        //权限验证
        if (!user.getRole().getNumberInsert()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        order.setCreateTime(new Date());
        order.setParentId(userId);
        int res = orderMapper.add(order);
        if (res == 1) {
            Order order1 = new Order();
            order1.setPage(0);
            order1.setOrder("id");
            List<Order> list = orderMapper.query(order1);
            return new Result(1,"添加成功",list, CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"添加失败",null,null);
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result deleteOrder(Order order, HttpServletRequest request){
        //用户验证
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        //权限验证
        if (!user.getRole().getOrderDelete()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        orderDetailMapper.delete(new OrderDetail(null, null, null, null, null, order.getId()));
        int res = orderMapper.delete(order);
        if (res == 1) {
            Order order1 = new Order();
            order1.setPage(0);
            order1.setOrder("id");
            List<Order> list = orderMapper.query(order1);
            return new Result(1,"删除成功",list, CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"删除失败",null,null);
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result editOrder(Order order, HttpServletRequest request){
        //用户验证
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        //权限验证
        if (!user.getRole().getOrderUpdate()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        int res = orderMapper.update(order);
        if (res == 1) {
            Order order1 = new Order();
            order1.setPage(0);
            order1.setOrder("id");
            List<Order> list = orderMapper.query(order1);
            return new Result(1,"修改成功",list, CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"修改失败",null,null);
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result listOrder(Order order, HttpServletRequest request){
        //用户验证
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        //权限验证
        if (!user.getRole().getOrderSelect()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        order.setPage(order.getPage() * 10);
        List<Order> list = orderMapper.query(order);
        return new Result(1,"查询成功",list, CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
    }
}

