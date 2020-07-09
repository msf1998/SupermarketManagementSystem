package com.mfs.sms.mapper;

import com.mfs.sms.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    int add(Order order);
    int addAndReturnId(Order order);
    int delete(Order order);
    int update(Order order);
    List<Order> query(Order order);
    Order queryById(Integer id);
}
