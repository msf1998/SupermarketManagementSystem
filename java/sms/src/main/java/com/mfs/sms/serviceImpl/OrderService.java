package com.mfs.sms.serviceImpl;

import com.mfs.sms.exception.DatabaseUpdateException;
import com.mfs.sms.mapper.*;
import com.mfs.sms.pojo.*;
import com.mfs.sms.pojo.Member;
import com.mfs.sms.pojo.to.SaleTo;
import com.mfs.sms.utils.CryptUtil;
import com.mfs.sms.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private UserService userService;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result getFinanceChart(HttpServletRequest request) throws Exception{
        //用户验证
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryByUsername(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getOrderSelect()) {
            return new Result(2,"抱歉,您没有该权限",null,null);
        }
        List<ChartData> list = new LinkedList<>();
        int year = new Date().getYear() + 1900;
        int month = new Date().getMonth() + 1;

        for (int day = 1; day <= 30; day ++) {
            Order order = new Order();
            order.setCreateTime(sdf.parse(year + "-" + month + "-" + day));
            List<Order> list1 = orderMapper.query(order);
            double sum = 0;
            for (Order o : list1) {
                sum += o.getSum();
            }
            list.add(new ChartData((-day)+"-",sum));
        }
        return new Result(1,"查询成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
    }
    /**
     * 创建订单完成交易
     * @param principal 已登录的用户主体
     * @param saleTo 交易过程中的传输对象
     * @return Result
     * */
    //创建订单完成交易
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20)
    public Result createOrder(Principal principal,@RequestBody SaleTo saleTo) throws DatabaseUpdateException {
        //验证参数合法性
        Result result = delegatingParameterObjectCheck(saleTo, "createOrder");
        if (result != null) {
            return result;
        }
        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }

        //非会员id全置1
        if (saleTo.getMemberId() == null || saleTo.getMemberId().equals("")) {
            saleTo.setMemberId("1");
        }
        //检查会员信息正确性
        Member member = memberMapper.queryById(saleTo.getMemberId());
        if (member == null) {
            return new Result(2,"会员不存在",null,null);
        }

        //获取购买的商品
        List<Product> list = new LinkedList<>();
        double sum = 0;
        for (int i = 0;i < saleTo.getProductId().length; i ++) {
            Product product = productMapper.queryById(saleTo.getProductId()[i]);
            if (product.getCount() < saleTo.getCount()[i]) {
                return new Result(2,"库存不足",null,null);
            }
            sum += product.getOutPrice() * saleTo.getCount()[i];
            product.setCount(product.getCount() - saleTo.getCount()[i]);
            int res = productMapper.update(product);
            if (res != 1) {
                throw new DatabaseUpdateException("修改商品失败");
            }
            product.setCount(saleTo.getCount()[i]);
            list.add(product);
        }
        if (saleTo.getSale() == true) {   //商品销售
            //创建订单
            Order order = new Order(null,"商品销售",sum,new Date(), member.getId()
                    ,null,user.getId(),null,null,null);
            int res = orderMapper.addAndReturnId(order);
            if (res != 1) {
                throw new DatabaseUpdateException("创建订单失败");
            }
            //System.out.println(order.getId());
            //创建订单详情
            OrderDetail orderDetail;
            for (Product product : list) {
                if(product.getTypeId() == 1) {
                    throw new DatabaseUpdateException("创建订单详情失败，赠品");
                }
                orderDetail = new OrderDetail(null,product.getId(),product.getName(),product.getOutPrice(),product.getCount(),order.getId());
                res = orderDetailMapper.add(orderDetail);
                if (res != 1) {
                    throw new DatabaseUpdateException("创建订单详情失败");
                }
            }
            member.setScore(member.getScore() + sum);
            res = memberMapper.update(member);
            if (res != 1) {
                throw new DatabaseUpdateException("修改用户积分失败");
            }
            return new Result(1,"付款成功",null,null);
        } else { //积分兑换
            if (member.getId().equals("1")) {
                return new Result(2,"该会员不能参加积分兑换",null,null);
            }
            if (sum <= member.getScore()) {
                //创建订单
                Order order = new Order(null,"积分兑换",sum,new Date(), member.getId(),null,user.getId(),null,null,null);
                int res = orderMapper.addAndReturnId(order);
                if (res != 1) {
                    throw new DatabaseUpdateException("创建订单失败");
                }
                //创建订单详情
                OrderDetail orderDetail;
                for (Product product : list) {
                    if(product.getTypeId() != 1) {
                        throw new DatabaseUpdateException("创建订单详情失败，正常销售商品");
                    }
                    orderDetail = new OrderDetail(null,product.getId(),product.getName(),product.getOutPrice(),product.getCount(),order.getId());
                    res = orderDetailMapper.add(orderDetail);
                    if (res != 1) {
                        throw new DatabaseUpdateException("创建订单详情失败");
                    }
                }
                member.setScore(member.getScore() - sum);
                res = memberMapper.update(member);
                if (res != 1) {
                    throw new DatabaseUpdateException("修改用户积分失败");
                }
                return new Result(1,"兑换成功",null,null);
            }
            return new Result(2,"积分不足",null,null);
        }

    }

    /**
     * 删除订单
     * @param principal 已登录的用户主体
     * @param order 删除参数
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result deleteOrder(Principal principal, Order order) throws DatabaseUpdateException {
        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getOrderDelete()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }


        int delete = orderDetailMapper.delete(new OrderDetail(null, null, null, null, null, order.getId()));
        int res = orderMapper.delete(order);
        if (res * delete  > 0) {
            return new Result(1,"删除成功",null,null);
        } else {
            throw new DatabaseUpdateException("删除订单或者订单详情失败");
        }
    }

    /**
     * 修改订单，仅能改备注
     * @param principal 已登录的用户主体
     * @param order 修改参数
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result editOrder(Principal principal, Order order){
        //参数检查
        Result result = delegatingParameterObjectCheck(order, "editOrder");
        if (result != null) {
            return result;
        }

        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getOrderUpdate()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        int res = orderMapper.update(order);
        if (res == 1) {
            return new Result(1,"修改成功",null,null);
        } else {
            return new Result(2,"修改失败",null,null);
        }
    }

    /**
     * 获取订单列表（可分页）
     * @param principal 已登录的用户主体
     * @param order 查询参数
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result listOrder(Principal principal, Order order){
        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getOrderSelect()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        //查询
        if (order.getPage() != null) {
            order.setPage(order.getPage() * 10);
        }
        List<Order> list = orderMapper.query(order);
        return new Result(1,"查询成功",list, null);
    }

    /**
     * 根据业务场景检查参数对象
     * @param object 要检查的参数对象
     * @param context 业务场景
     * @return Result
     * */
    private Result delegatingParameterObjectCheck(Object object,String context) {
        switch (context) {
            case "createOrder" : {
                SaleTo saleTo = (SaleTo)object;
                if (saleTo.getMemberId() != null) {
                    if (!saleTo.getMemberId().matches("\\d{11}")) {
                        return new Result(2,"会员id必须是11位手机号",null,null);
                    }
                }
                if (saleTo.getSale() == null) {
                    return new Result(2,"未指定业务场景",null,null);
                }
                if (saleTo.getProductId() == null || saleTo.getCount() == null || saleTo.getProductId().length == 0 ||
                        saleTo.getCount().length == 0 || saleTo.getProductId().length != saleTo.getCount().length) {
                    return new Result(2,"产品参数有误",null,null);
                }
                return null;
            }
            case "editOrder" : {
                Order order = (Order) object;
                if (order.getId() == null || order.getId() < 0) {
                    return new Result(2,"订单编号不合法",null,null);
                }
                if (order.getDescribe() == null || order.getDescribe().equals("") || order.getDescribe().length() > 255) {
                    return new Result(3,"备注不合法",null,null);
                }
                order.setCreateTime(null);
                order.setParentId(null);
                order.setCreatorId(null);
                order.setSum(null);
                break;
            }
        }
        return null;
    }
}

