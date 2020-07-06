package com.mfs.sms.mapper;

import com.mfs.sms.pojo.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    int add(OrderDetail orderDetail);
    int delete(OrderDetail orderDetail);
    int update(OrderDetail orderDetail);
    List<OrderDetail> query(OrderDetail orderDetail);
    OrderDetail queryById(Integer id);
}
