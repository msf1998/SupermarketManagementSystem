package com.mfs.sms.service;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
                user.setPassword(user.getPassword() == null ? CryptUtil.getMessageDigestByMD5("123" + salt) : CryptUtil.getMessageDigestByMD5(user.getPassword() + salt));
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

        int res = userMapper.add(user);
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
        if (!user1.getRole().getUserUpdate()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        int res = userMapper.update(user);
        if (res == 1) {
            User user2 = new User();
            user2.setOrder("id");
            user2.setPage(0);
            user2.setDeleted(false);
            List<User> list = userMapper.query(user2);
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
        //System.out.println(list);
        return new Result(1,"查询成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Result getMe(HttpServletRequest request) {
        String userId = RequestUtil.getUserId(request);
        //System.out.println(userId);
        User user1 = userMapper.queryById(userId);
        if (user1 == null) {
            return new Result(4,"用户不存在",null,null);
        }
        user1.setPassword(null);
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
    public Result register(User user) {
        User user1 = userMapper.queryById(user.getId());
        if (user1 != null) {
            return new Result(2,"用户已存在",null,null);
        }
        String salt = SaltGenerator.generatorSalt();
        user.setPassword(CryptUtil.getMessageDigestByMD5(user.getPassword() + salt));
        user.setHead("default.jpg");
        user.setCreateTime(new Date());
        user.setSalt(salt);
        if (user.getConfirmCode().equals("admin")) {
            user.setRoleId(1);
            int res = userMapper.add(user);
            if (res == 1) {
                return new Result(1,"注册成功",null,null);
            }
            return new Result(2,"注册失败",null,null);
        } else {
            return new Result(2,"注册失败",null,null);
        }
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
