package com.mfs.sms.service;

import com.google.zxing.qrcode.encoder.QRCode;
import com.mfs.sms.mapper.ProductMapper;
import com.mfs.sms.mapper.UserMapper;
import com.mfs.sms.pojo.CompareObj;
import com.mfs.sms.pojo.Product;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.pojo.User;
import com.mfs.sms.utils.CryptUtil;
import com.mfs.sms.utils.QCodeUtil;
import com.mfs.sms.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserMapper userMapper;

    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result createPurchaseOrder(Product product,HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductUpdate()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        return new Result(2,"删除失败",null,null);
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result deleteCountProduct(Product product,HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductDelete()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        int res = productMapper.delete(product);
        if (res == 1) {
            List<Product> list = productMapper.queryGreaterThan(new CompareObj("warn_count","count"));
            return new Result(1,"删除成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"删除失败",null,null);
        }
    }
    //修改快过期的商品
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result editSelfLifeProduct(Product product, HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductUpdate()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        int res = productMapper.update(product);
        if (res == 1) {
            List<Product> list = productMapper.queryBySelfLife();
            return new Result(1,"修改成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"修改失败",null,null);
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result selfLifeWarn(HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductSelect()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        List<Product> list = productMapper.queryBySelfLife();
        return new Result(1,"查询成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result countWarn(CompareObj compareObj,HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductSelect()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        List<Product> list = productMapper.queryGreaterThan(compareObj);
        //System.out.println(list);
        return new Result(1,"查询成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
    }
    @Transactional(isolation = Isolation.DEFAULT,timeout = 5)
    public Result addProduct(Product product, HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductInsert()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        product.setId(product.getId() + new Date().getTime());
        product.setPhoto("default.jpg");
        product.setParentId(user.getId());
        product.setInTime(new Date());
        //创建商品码
        String qrCode = QCodeUtil.createQRCode(product.getId());
        product.setQCode(qrCode);
        int res = productMapper.add(product);
        if (res == 1) {
            Product product1 = new Product();
            product1.setOrder("name");
            product1.setPage(0);
            List<Product> list = productMapper.query(product1);
            //更新token
            return new Result(1,"添加成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"添加失败",null,null);
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result deleteProduct(Product product,HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil .getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductDelete()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        int res = productMapper.delete(product);
        if (res == 1) {
            Product product1 = new Product();
            product1.setOrder("name");
            product1.setPage(0);
            List<Product> list = productMapper.query(product1);
            return new Result(1,"删除成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"删除失败",null,null);
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result editProduct(Product product, HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductUpdate()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        int res = productMapper.update(product);
        if (res == 1) {
            Product product1 = new Product();
            product1.setOrder("name");
            product1.setPage(0);
            List<Product> list = productMapper.query(product1);
            return new Result(1,"修改成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"修改失败",null,null);
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result listProducts(Product product,HttpServletRequest request) {
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductSelect()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        List<Product> list = productMapper.query(product);
        return new Result(1,"查询成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
    }
}
