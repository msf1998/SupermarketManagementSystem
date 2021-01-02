package com.mfs.sms.serviceImpl;

import com.mfs.sms.mapper.OrderDetailMapper;
import com.mfs.sms.mapper.UserMapper;
import com.mfs.sms.pojo.OrderDetail;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.pojo.User;
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
public class OrderDetailService {
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private UserMapper userMapper;

    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result listOrderDetail(OrderDetail orderDetail, HttpServletRequest request) {
        //用户验证
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryByUsername(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        //权限验证
        if (!user.getRole().getMemberSelect()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        List<OrderDetail> list = orderDetailMapper.query(orderDetail);
        return new Result(1,"查询成功",list, CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
    }
}
