package com.mfs.sms.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mfs.sms.mapper.UserMapper;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.pojo.User;
import com.mfs.sms.utils.CryptUtil;
import com.mfs.sms.utils.RequestUtil;
import com.mfs.sms.utils.SaltGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Transactional(isolation = Isolation.DEFAULT,timeout = 5)
    public Result addUser(User user, HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user1 = userMapper.queryById(userId);
        if (user1 == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user1.getRole().getUserInsert()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        User user3 = userMapper.queryById(user.getId());
        if(user3 != null) {
            if (user3.getDeleted()) {
                String salt = SaltGenerator.generatorSalt();
                user.setSalt(salt);
                user.setPassword(user.getPassword() == null ? CryptUtil.getMessageDigestByMD5(CryptUtil.getMessageDigestByMD5("123") + salt) : CryptUtil.getMessageDigestByMD5(user.getPassword() + salt));
                user.setHead("default.jpg");
                user.setCreateTime(new Date());
                user.setDeleted(false);
                int res = userMapper.update(user);
                if (res == 1) {
                    User user2 = new User();
                    user2.setOrder("id");
                    user2.setPage(0);
                    user2.setDeleted(false);
                    List<User> list = userMapper.query(user2);
                    //更新token
                    return new Result(1,"添加成功",list, CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
                } else {
                    return new Result(2,"添加失败",null,null);
                }
            }
            return new Result(2,"该账号已存在",null,null);
        }
        String salt = SaltGenerator.generatorSalt();
        user.setSalt(salt);
        user.setPassword(user.getPassword() == null ? CryptUtil.getMessageDigestByMD5("123" + salt) : CryptUtil.getMessageDigestByMD5(user.getPassword() + salt));
        user.setHead("default.jpg");
        user.setCreateTime(new Date());
        user.setDeleted(false);

        int res = userMapper.add(user);
        if (res == 1) {
            User user2 = new User();
            user2.setOrder("id");
            user2.setPage(0);
            user2.setDeleted(false);
            List<User> list = userMapper.query(user2);
            for (int i = 0;i < list.size(); i ++) {
                list.get(i).setPassword(null);
            }
            //更新token
            return new Result(1,"添加成功",list, CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"添加失败",null,null);
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result deleteUser(User user,HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil .getUserId(request);
        User user1 = userMapper.queryById(userId);
        if (user1 == null) {
            return new Result(4,"用户不存在",null,null);
        }
        //权限验证
        if (!user1.getRole().getUserDelete()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        if (user.getId().equals(userId)) {
            return new Result(2,"不能删除自己",null,null);
        }
        if (user.getId().equals("1")) {
            return new Result(2,"不能删除root用户",null,null);
        }
        user.setDeleted(true);
        int res = userMapper.update(user);
        if (res == 1) {
            User user2 = new User();
            user2.setOrder("id");
            user2.setPage(0);
            user2.setDeleted(false);
            List<User> list = userMapper.query(user2);
            for (int i = 0;i < list.size(); i ++) {
                list.get(i).setPassword(null);
            }
            return new Result(1,"删除成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"删除失败",null,null);
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result editUser(User user, HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user1 = userMapper.queryById(userId);
        if (user1 == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (user.getId().equals("1")) {
            return new Result(2,"不能修改root用户",null,null);
        }
        if (!user1.getRole().getUserUpdate()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        if (user.getPassword() != null && !user.getPassword().equals("")) {
            String salt = SaltGenerator.generatorSalt();
            user.setPassword(CryptUtil.getMessageDigestByMD5(user.getPassword() + salt));
            user.setSalt(salt);
        }
        int res = userMapper.update(user);
        if (res == 1) {
            User user2 = new User();
            user2.setOrder("id");
            user2.setPage(0);
            user2.setDeleted(false);
            List<User> list = userMapper.query(user2);
            for (int i = 0;i < list.size(); i ++) {
                list.get(i).setPassword(null);
            }
            return new Result(1,"修改成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"修改失败",null,null);
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result listUser(User user,HttpServletRequest request) {
        String userId = RequestUtil.getUserId(request);
        User user1 = userMapper.queryById(userId);
        //System.out.println(user1);
        if (user1 == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user1.getRole().getUserSelect()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        if (user.getPage() != null) {
            user.setPage(user.getPage() * 10);
        }
        user.setDeleted(false);
        List<User> list = userMapper.query(user);
        for (int i = 0;i < list.size(); i ++) {
            list.get(i).setPassword(null);
        }
        //System.out.println(list);
        return new Result(1,"查询成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Result editMe(MultipartFile head,String password,HttpServletRequest request) throws Exception{
        String userId = RequestUtil.getUserId(request);
        User user1 = userMapper.queryById(userId);
        if (user1 == null) {
            return new Result(4,"用户不存在",null,null);
        }
        User u = new User();
        u.setId(userId);
        if (head != null) {
            String name = head.getOriginalFilename();
            if (!(name.endsWith(".jpg") || name.endsWith(".png"))) {
                return new Result(2,"仅支持.jpg或者.png格式的图片",null,null);
            }
            name = userId + "" + new Date().getTime() + (name.endsWith(".jpg") ? ".jpg" : ".png");
            File file = new File("E:/images/sms/head/" + name);
            if (file.exists()) {
                file.createNewFile();
            }
            InputStream is = head.getInputStream();
            OutputStream os = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int n = 0;
            while ((n = is.read(b)) >= 0) {
                os.write(b,0,n);
            }
            is.close();
            os.close();
            u.setHead(name);
            user1.setHead(name);
        }
        if (password != null && !password.equals("")) {
            String salt = SaltGenerator.generatorSalt();
            u.setSalt(salt);
            u.setPassword(CryptUtil.getMessageDigestByMD5(password + "" + salt));
            user1.setSalt(salt);
            user1.setPassword(CryptUtil.getMessageDigestByMD5(password + "" + salt));
        }
        int res = userMapper.update(u);
        if (res == 1) {
            return new Result(1,"修改成功",user1,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(1,"修改失败",null,null);
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Result getMe(HttpServletRequest request) {
        String userId = RequestUtil.getUserId(request);
        //System.out.println(userId);
        User user1 = userMapper.queryById(userId);
        if (user1 == null) {
            return new Result(4,"用户不存在",null,null);
        }

        return new Result(1,"查询成功",user1,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ,timeout = 5)
    public Result login(User user, HttpServletResponse response) {
        User user1 = userMapper.queryById(user.getId());
        if (user1 == null) {
            return new Result(2,"用户名不存在",null,null);
        }
        user.setPassword(CryptUtil.getMessageDigestByMD5(user.getPassword() + user1.getSalt()));
        List<User> res = userMapper.query(user);
        if (res.size() == 1) {
            return new Result(1,"登陆成功",null,CryptUtil.encryptByDES(res.get(0).getId() + "##" + new Date().getTime()));
        }
        return new Result(2,"登陆失败",res,null);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 5)
    public Result register(User user) throws Exception{
        User user1 = userMapper.queryById(user.getId());
        if (user1 != null) {
            return new Result(2,"用户已存在",null,null);
        }
        String salt = SaltGenerator.generatorSalt();
        user.setPassword(CryptUtil.getMessageDigestByMD5(user.getPassword() + salt));
        user.setHead("default.jpg");
        user.setCreateTime(new Date());
        user.setSalt(salt);
        user.setDeleted(false);
        //验证证书
        String content = CryptUtil.getAuthorityContent(user.getConfirmCode());

        if (content == null) {
            return new Result(2,"证书错误,请购买正版证书",null,null);
        }
        String[] split = content.split("##");
        if (split.length != 3) {
            return new Result(2,"证书错误,请购买正版证书",null,null);
        }
        if (!split[0].equals("MFS")) {
            return new Result(2,"证书错误,请购买正版证书",null,null);
        }
        if ((new Date().getTime() - Long.valueOf(split[1])) > 24 * 60 * 60 * 1000) {
            return new Result(2,"证书已过期,请购买正版证书",null,null);
        }
        if (!split[2].equals("admin")) {
            return new Result(2,"证书错误,请购买正版证书",null,null);
        }
        user.setRoleId(1);
        int res = userMapper.add(user);
        if (res == 1) {
            return new Result(1,"注册成功",null,null);
        }
        return new Result(2,"注册失败",null,null);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED,timeout = 5)
    public Result checkExist(User user) {
        User user1 = userMapper.queryById(user.getId());
        if (user1 != null) {
            return new Result(2,"用户已存在",null,null);
        }
        return new Result(1,"",null,null);
    }
}
