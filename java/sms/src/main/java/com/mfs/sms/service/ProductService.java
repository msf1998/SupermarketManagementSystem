package com.mfs.sms.service;

import com.mfs.sms.mapper.ProductMapper;
import com.mfs.sms.mapper.UserMapper;
import com.mfs.sms.pojo.CompareObj;
import com.mfs.sms.pojo.Product;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.pojo.User;
import com.mfs.sms.utils.CryptUtil;
import com.mfs.sms.utils.ExcelUtil;
import com.mfs.sms.utils.QCodeUtil;
import com.mfs.sms.utils.RequestUtil;
import jdk.nashorn.internal.runtime.regexp.RegExp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserMapper userMapper;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    //修改商品图片
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result editProductPhoto(HttpServletRequest request, MultipartFile file,String id) throws Exception {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductInsert()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        String name = file.getOriginalFilename();
        Pattern compile = Pattern.compile("^.*\\.png$|^.*\\.jpg$");
        Matcher matcher = compile.matcher(name);
        if (!matcher.matches()) {
            return new Result(2,"文件格式不正确",null,null);
        }
        String photoName = UUID.randomUUID().toString() + new Date().getTime() + (name.endsWith(".png") ? ".png" : "jpg");
        String path = "E:/images/sms/product/" + photoName;
        File f = new File(path);
        if (!f.exists()) {
            f.createNewFile();
        }
        OutputStream os  = new FileOutputStream(f);
        InputStream is = file.getInputStream();
        int n = 0;
        byte[] b = new byte[1024];
        while ((n = is.read(b)) > 0) {
            os.write(b,0,n);
        }
        is.close();
        os.close();
        Product product = new Product();
        product.setId(id);
        product.setPhoto(photoName);
        int res = productMapper.update(product);
        if (res == 1) {
            Product product1 = new Product();
            product1.setOrder("name");
            product1.setPage(0);
            List<Product> l = productMapper.query(product1);
            return new Result(1,"修改成功",l,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(1,"修改失败",null,null);
        }
    }
    //导入进货单
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result leadingInPurchaseOrder(HttpServletRequest request, MultipartFile file) throws Exception {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductInsert()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        String name = file.getOriginalFilename();
        Pattern compile = Pattern.compile("^.*\\.xls$");
        Matcher matcher = compile.matcher(name);
        if (!matcher.matches()) {
            return new Result(2,"文件格式不正确",null,null);
        }
        String path = "E:/images/sms/product/excel/leading-in/" + userId + new Date().getTime() + ".xls";
        File f = new File(path);
        if (!f.exists()) {
            f.createNewFile();
        }
        OutputStream os  = new FileOutputStream(f);
        InputStream is = file.getInputStream();
        int n = 0;
        byte[] b = new byte[1024];
        while ((n = is.read(b)) > 0) {
            os.write(b,0,n);
        }
        is.close();
        os.close();
        List<Map<String, String>> list = ExcelUtil.resd(path);
        for (Map<String,String> m : list) {
            String id = m.get("编号") + new Date().getTime();
            Product product = new Product(id,m.get("商品名"),"default.jpg",m.get("供货商"),
                    sdf.parse(m.get("生产日期")),Integer.parseInt(m.get("保质期(月)")),Integer.parseInt(m.get("提醒(天)")),
                    Integer.parseInt(m.get("数量")),Integer.parseInt(m.get("预警库存")),new Date(),Double.parseDouble(m.get("进价")),
            Double.parseDouble(m.get("售价")),userId,null,Integer.parseInt(m.get("类型编号")),null,QCodeUtil.createQRCode(id),null,null);
            int res = productMapper.add(product);
            if (res != 1) {
                int i = 1 / 0;
            }
        }
        Product product1 = new Product();
        product1.setOrder("name");
        product1.setPage(0);
        List<Product> l = productMapper.query(product1);
        return new Result(1,"导入成功",l,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
    }
    //创建进货单
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result createPurchaseOrder(HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductUpdate()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        List<Product> list = productMapper.queryGreaterThan(new CompareObj("warn_count","count"));
        String path = "E:/images/sms/product/leading-out/" + userId + new Date().getTime() + ".xls";
        String file = ExcelUtil.write(path, list);
        int res = 1;
        for (Product p : list) {
            p.setTypeId(3);
            res = res * productMapper.update(p);
        }
        if (file.contains(userId) && res == 1){
            return new Result(1,"创建成功",file,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            int i = 1/0;
            return new Result(2,"创建excel失败",null,null);
        }
    }
    //删除库存过少的商品
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result deleteCountLessThanWarnCountProduct(Product product,HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductDelete()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        //删除二维码
        Product product1 = productMapper.queryById(product.getId());
        File file = new File("E:/images/sms/product/qcode/" + product1.getQCode());
        if (file.exists()) {
            file.delete();
        }
        //删除图片
        file = new File("E:/images/sms/product/" + product1.getPhoto());
        if (file.exists()) {
            file.delete();
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
    public Result editWillGoBadProduct(Product product, HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryById(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductUpdate()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        //将修改的商品类型改为促销，并把提醒日期值变为0
        product.setTypeId(2);
        product.setWarnBefore(0);
        int res = productMapper.update(product);
        if (res == 1) {
            List<Product> list = productMapper.queryBySelfLife();
            return new Result(1,"修改成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"修改失败",null,null);
        }
    }
    //获取将要过期的商品列表
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result listWillGoBadProduct(HttpServletRequest request) {
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
    //获取库存量小于提醒值的商品列表
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result listCountLessThanWarnCountProduct(CompareObj compareObj,HttpServletRequest request) {
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


    //增删改查
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
        //删除二维码
        Product product1 = productMapper.queryById(product.getId());
        File file = new File("E:/images/sms/product/qcode/" + product1.getQCode());
        if (file.exists()) {
            file.delete();
        }
        //删除图片
        file = new File("E:/images/sms/product/" + product1.getPhoto());
        if (file.exists()) {
            file.delete();
        }
        int res = productMapper.delete(product);
        if (res == 1) {
            product1 = new Product();
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
