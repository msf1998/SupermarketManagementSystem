package com.mfs.sms.service;

import com.mfs.sms.mapper.*;
import com.mfs.sms.pojo.*;
import com.mfs.sms.pojo.Number;
import com.mfs.sms.pojo.to.SaleTo;
import com.mfs.sms.utils.CryptUtil;
import com.mfs.sms.utils.RequestUtil;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    private NumberMapper numberMapper;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result getFinanceChart(HttpServletRequest request) throws Exception{
        //用户验证
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
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
     * @Author dyz
     * */
    //创建订单完成交易
    @Transactional(isolation = Isolation.REPEATABLE_READ,timeout = 5)
    public Result createOrder(SaleTo saleTo, HttpServletRequest request) {
        //用户验证
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        //非会员id全置1
        if (saleTo.getNumberId() == null || saleTo.getNumberId().equals("")) {
            saleTo.setNumberId("");
        }
        //检查会员信息正确性
        Number number = numberMapper.queryById(saleTo.getNumberId());
        if (number == null) {
            return new Result(2,"会员Id有误",null,null);
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
                int j = 1 / 0;
            }
            product.setCount(saleTo.getCount()[i]);
            list.add(product);
        }
        if (saleTo.getSale() == true) {   //商品销售
            //创建订单
            Order order = new Order(null,"商品销售",sum,new Date(),number.getId(),null,userId,null,null,null);
            int res = orderMapper.addAndReturnId(order);
            if (res != 1) {
                int i = 1/0;
            }
            //System.out.println(order.getId());
            //创建订单详情
            OrderDetail orderDetail;
            for (Product product : list) {
                if(product.getTypeId() == 1) {
                    int i = 1 / 0;
                }
                orderDetail = new OrderDetail(null,product.getId(),product.getName(),product.getOutPrice(),product.getCount(),order.getId());
                res = orderDetailMapper.add(orderDetail);
                if (res != 1) {
                    int i = 1/0;
                }
            }
            number.setScore(number.getScore() + sum);
            res = numberMapper.update(number);
            if (res != 1) {
                int i = 1 / 0;
            }
            return new Result(1,"付款成功",null,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else { //积分兑换
            if (number.getId().equals("1")) {
                return new Result(2,"该会员不能参加积分兑换",null,null);
            }
            if (sum <= number.getScore()) {
                //创建订单
                Order order = new Order(null,"积分兑换",sum,new Date(),number.getId(),null,userId,null,null,null);
                int res = orderMapper.addAndReturnId(order);
                if (res != 1) {
                    int i = 1/0;
                }
                //创建订单详情
                OrderDetail orderDetail;
                for (Product product : list) {
                    if(product.getTypeId() != 1) {
                        int i = 1 / 0;
                    }
                    orderDetail = new OrderDetail(null,product.getId(),product.getName(),product.getOutPrice(),product.getCount(),order.getId());
                    res = orderDetailMapper.add(orderDetail);
                    if (res != 1) {
                        int i = 1/0;
                    }
                }
                number.setScore(number.getScore() - sum);
                res = numberMapper.update(number);
                if (res != 1) {
                    int i = 1 / 0;
                }
                return new Result(1,"兑换成功",null,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
            } else {
                return new Result(2,"积分不足",null,null);
            }
        }

    }
    //增删改查
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

