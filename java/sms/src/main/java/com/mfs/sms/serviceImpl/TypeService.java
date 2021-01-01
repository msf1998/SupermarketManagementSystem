package com.mfs.sms.serviceImpl;

import com.mfs.sms.mapper.ProductMapper;
import com.mfs.sms.mapper.TypeMapper;
import com.mfs.sms.mapper.UserMapper;
import com.mfs.sms.pojo.Product;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.pojo.Type;
import com.mfs.sms.pojo.User;
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
public class TypeService {

    @Autowired
    private TypeMapper typeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserService userService;

    @Transactional(isolation = Isolation.DEFAULT,timeout = 5)
    public Result addType(Type type, HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryByUsername(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getTypeInsert()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        type.setId(null);
        type.setParentId(user.getUsername());
        type.setCreateTime(new Date());
        int res = typeMapper.add(type);
        if (res == 1) {
            Type type1 = new Type();
            type1.setOrder("id");
            type1.setPage(0);
            List<Type> list = typeMapper.query(type1);
            //更新token
            return new Result(1,"添加成功",list, CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"添加失败",null,null);
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result deleteType(Type type,HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil .getUserId(request);
        User user = userMapper.queryByUsername(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        //权限验证
        if (!user.getRole().getTypeDelete()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        //查看该类型是否正在被使用
        Product p = new Product();
        p.setTypeId(type.getId());
        List<Product> list1 = productMapper.query(p);
        if (!(list1 == null || list1.size() == 0)) {
            return new Result(2,"该类型正在被"+list1.size()+"个商品使用",null,null);
        }
        int res = typeMapper.delete(type);
        if (res == 1) {
            Type type1 = new Type();
            type1.setOrder("id");
            type1.setPage(0);
            List<Type> list = typeMapper.query(type1);
            return new Result(1,"删除成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"删除失败",null,null);
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result editType(Type type, HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryByUsername(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getTypeUpdate()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        int res = typeMapper.update(type);
        if (res == 1) {
            Type type1 = new Type();
            type1.setOrder("id");
            type1.setPage(0);
            List<Type> list = typeMapper.query(type1);
            return new Result(1,"修改成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"修改失败",null,null);
        }
    }

    /**
     * 获取类型列表
     * @param type,查询参数，可以设置部分属性，也可以不加设置
     * @param principal 已登录的用户主体
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result listTypes(Principal principal, Type type) {
        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getTypeSelect()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        //如果需要分页的话设置起始位置，偏移量默认为10
        if (type.getPage() != null) {
            type.setPage(type.getPage() * 10);
        }
        List<Type> list = typeMapper.query(type);
        System.out.println(list);
        return new Result(1,"查询成功",list,null);
    }
}
