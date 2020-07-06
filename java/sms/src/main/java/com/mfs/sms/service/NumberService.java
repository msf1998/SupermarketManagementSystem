package com.mfs.sms.service;

import com.mfs.sms.mapper.NumberMapper;
import com.mfs.sms.mapper.UserMapper;
import com.mfs.sms.pojo.Number;
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
public class NumberService {
    @Autowired
    private NumberMapper numberMapper;
    @Autowired
    private UserMapper userMapper;

    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 5)
    public Result addNumber(Number number, HttpServletRequest request){
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
        //查看是否已注册
        Number number2 = numberMapper.queryById(number.getPhone());
        if (number2 != null) {
            if (number2.getDeleted() == true) {
                number.setDeleted(false);
                number.setId(number.getPhone());
                number.setCreateTime(new Date());
                number.setParentId(userId);
                number.setScore(0.0);
                int res = numberMapper.update(number);
                if (res == 1) {
                    Number number1 = new Number();
                    number1.setDeleted(false);
                    number1.setPage(0);
                    number1.setOrder("name");
                    List<Number> list = numberMapper.query(number1);
                    return new Result(1,"添加成功",list, CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
                } else {
                    return new Result(2,"添加失败",null,null);
                }
            }
            return new Result(2,"该手机号已注册",null,null);
        }
        number.setId(number.getPhone());
        number.setCreateTime(new Date());
        number.setParentId(userId);
        number.setScore(0.0);
        int res = numberMapper.add(number);
        if (res == 1) {
            Number number1 = new Number();
            number1.setDeleted(false);
            number1.setPage(0);
            number1.setOrder("name");
            List<Number> list = numberMapper.query(number1);
            return new Result(1,"添加成功",list, CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"添加失败",null,null);
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result deleteNumber(Number number, HttpServletRequest request){
        //用户验证
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        //权限验证
        if (!user.getRole().getNumberDelete()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        if (number.getId().equals("1")) {
            return new Result(2,"改会员不允许删除",null,null);
        }
        number.setDeleted(true);
        int res = numberMapper.update(number);
        if (res == 1) {
            Number number1 = new Number();
            number1.setPage(0);
            number1.setOrder("name");
            number1.setDeleted(false);
            List<Number> list = numberMapper.query(number1);
            return new Result(1,"删除成功",list, CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"删除失败",null,null);
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result editNumber(Number number, HttpServletRequest request){
        //用户验证
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        //权限验证
        if (!user.getRole().getNumberUpdate()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        int res = numberMapper.update(number);
        if (res == 1) {
            Number number1 = new Number();
            number1.setPage(0);
            number1.setOrder("name");
            number1.setDeleted(false);
            List<Number> list = numberMapper.query(number1);
            return new Result(1,"修改成功",list, CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"修改失败",null,null);
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result listNumber(Number number, HttpServletRequest request){
        //用户验证
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        //权限验证
        if (!user.getRole().getNumberSelect()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        number.setDeleted(false);
        number.setPage(number.getPage() * 10);
        List<Number> list = numberMapper.query(number);
        return new Result(1,"查询成功",list, CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
    }
}
