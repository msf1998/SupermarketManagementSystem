package com.mfs.sms.serviceImpl;

import com.mfs.sms.mapper.OrderDetailMapper;
import com.mfs.sms.mapper.UserMapper;
import com.mfs.sms.pojo.OrderDetail;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.pojo.User;
import com.mfs.sms.pojo.to.SaleTo;
import com.mfs.sms.utils.CryptUtil;
import com.mfs.sms.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private UserService userService;

    /**
     * 查询订单详情
     * @param principal 已登录的用户主体
     * @param orderDetail 查询参数
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result listOrderDetail(Principal principal, OrderDetail orderDetail) {
        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getMemberSelect()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        List<OrderDetail> list = orderDetailMapper.query(orderDetail);
        return new Result(1,"查询成功",list,null);
    }
}
