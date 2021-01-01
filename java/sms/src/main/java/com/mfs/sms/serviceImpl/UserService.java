package com.mfs.sms.serviceImpl;

import com.mfs.sms.exception.RedisUpdateException;
import com.mfs.sms.mapper.RoleMapper;
import com.mfs.sms.mapper.UserMapper;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.pojo.Role;
import com.mfs.sms.pojo.User;
import com.mfs.sms.utils.CryptUtil;
import com.mfs.sms.utils.RequestUtil;
import com.mfs.sms.utils.SaltGenerator;
import com.mfs.sms.utils.log.LogUtil;
import com.mfs.sms.utils.redis.MapClassMapObjectUtil;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private LogUtil logUtil;

    @Transactional(isolation = Isolation.DEFAULT,timeout = 20)
    public Result addUser(User user, HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user1 = userMapper.queryByUsername(userId);
        if (user1 == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user1.getRole().getUserInsert()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        //授予的角色的权限等级不能高于操作这本身
        Role role = roleMapper.queryById(user.getRoleId());
        if (role.compareTo(user1.getRole()) == 1) {
            return new Result(2,"授予权限过高",null,null);
        }
        User user3 = userMapper.queryByUsername(user.getUsername());
        if(user3 != null) {
            if (user3.getDeleted()) {
                String salt = SaltGenerator.generatorSalt();
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

    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result deleteUser(User user,HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil .getUserId(request);
        User user1 = userMapper.queryByUsername(userId);
        if (user1 == null) {
            return new Result(4,"用户不存在",null,null);
        }
        //权限验证
        if (!user1.getRole().getUserDelete()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        if (user.getUsername().equals(userId)) {
            return new Result(2,"不能删除自己",null,null);
        }
        if (user.getUsername().equals("1")) {
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

    /**
     * 修改用户信息
     * @param principal 当前登录的用户主体
     * @param user 要修改的用户信息
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result editUser(Principal principal, User user) throws RedisUpdateException {
        //验证输入的合法性
        Result result = delegatingCheckUser(user, "editUser");
        if (result != null) {
            return result;
        }
        //验证登录合法性
        String username = principal.getName();
        User user1 = quicklyGetUserByUsername(username);
        if (user1 == null) {
            return new Result(4,"用户不存在",null,null);
        }

        //鉴权
        if (!user1.getRole().getUserUpdate()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        //修改数据库
        int res = userMapper.update(user);
        if (res == 1) {
            //清除缓存
            if (redisTemplate.hasKey("login.user." + user.getUsername())) {
                Boolean delete = redisTemplate.delete("login.user." + user.getUsername());
                if (!delete) {
                    throw new RedisUpdateException("删除用户缓存失败，user : " + user.getUsername());
                }
            }
            return new Result(1,"修改成功",null,null);

        } else {
            return new Result(2,"修改失败",null,null);
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result listUser(User user,HttpServletRequest request) {
        String userId = RequestUtil.getUserId(request);
        User user1 = userMapper.queryByUsername(userId);
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

    /**
     * 修改当前登录的用户密码
     * @param principal 已登录用户主体
     * @param password 修改的用户密码
     * @return Result
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20)
    public Result editPassword(Principal principal,String password) {
        String username = principal.getName();
        User user = new User();
        user.setUsername(username);
        user.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password));

        //验证输入合法性
        Result result = delegatingCheckUser(user, "editPassword");
        if (result != null) {
            return result;
        }

        //修改数据库
        int update = userMapper.update(user);
        if (update == 1) {
            return new Result(1,"修改成功，下次登陆生效",null,null);
        }
        return new Result(2,"修改失败",null,null);
    }

    /**
     * 修改当前登录用户的头像
     * @param principal 登录主体
     * @param head 上传的图片
     * @return Result
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20)
    public Result editHead(Principal principal,MultipartFile head) throws RedisUpdateException, IOException {
        //验证用户合法性
        String username = principal.getName();
        User user = quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        String name = head.getOriginalFilename();
        //验证上传合法性
        if (head == null || !(name.endsWith(".jpg") || name.endsWith(".png"))) {
            return new Result(2,"仅支持jpg和png格式的图片",null,null);
        }
        if (!(name.endsWith(".jpg") || name.endsWith(".png"))) {
            return new Result(2,"仅支持.jpg或者.png格式的图片",null,null);
        }
        //保存图片到磁盘
        name = username + (name.endsWith(".jpg") ? ".jpg" : ".png");
        File file = new File("E:/images/sms/head/" + name);
        if (!file.exists()) {
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

        //修改数据库中用户的头像信息
        if (user.getHead().equals("default.jpg")) {
            user = new User();
            user.setUsername(username);
            user.setHead(name);
            int update = userMapper.update(user);
            if (update == 1) {
                Boolean delete = redisTemplate.delete("login.user." + username);
                if (delete) {
                    return new Result(1,"头像修改成功",null,null);
                }
                logUtil.log("删除缓存失败",this.getClass());
                return new Result(2,"修改成功,下次登陆生效",null,null);
            } else {
                boolean delete = file.delete();
                if (!delete) {
                    throw new RedisUpdateException("删除用户缓存失败，user : " + user.getUsername());
                }
                return new Result(2,"修改失败",null,null);
            }
        }
        return new Result(1,"头像修改成功",null,null);
    }

    /**
     * 获取当前以登陆的用户的详细信息
     * @param principal 当前已登录的用户主体
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result getMe(Principal principal) {
        String username = principal.getName();
        User user = quicklyGetUserByUsername(username);
        if (user != null) {
            return new Result(1,"查询成功",user,null);
        }
        return new Result(2,"查询失败",null,null);
    }

    /**
     * 快速获取指定用户（优先从redis缓存中获取，缓存中没有则从数据库中获取并存向缓存）
     * @param username 用户名
     * @return 如果找到则返回User对象，找不到则返回null
     * */
    User quicklyGetUserByUsername(String username) {
        //从缓存中获取用户信息
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        Object map = opsForValue.get("login.user." + username);
        //从缓存中获取的对象是LinkedHashMap类型的，所以要转化到我们需要的User类型
        User user = null;
        try {
            user = MapClassMapObjectUtil.map((Map<String, Object>) map, User.class);
        } catch (MapClassMapObjectUtil.TypeException e) {
            e.printStackTrace();
        }
        if (user != null) {
            redisTemplate.expire("login.user." + username,30,TimeUnit.MINUTES);
            return user;
        }
        //缓存中没有则从数据库获取
        User u = userMapper.queryByUsername(username);
        u.setPassword(null);
        if (user != null) {
            opsForValue.set("login.user." + username, u,30,TimeUnit.MINUTES);
            return u;
        }
        return null;
    }

    /**
     * 用户注册
     * @param user 要注册的用户基本信息，至少包含username,password,password1,name,confirmCode等基本属性
     * @return Result
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20)
    public Result register(User user) throws Exception{
        //检查user的合法性
        Result result = null;
        result = delegatingCheckUser(user,"register");
        if (result != null) {
            return result;
        }

        //检查用户是否已存在
        User user1 = userMapper.queryByUsername(user.getUsername());
        if (user1 != null) {
            return new Result(2,"用户已存在",null,null);
        }

        //验证证书
        String content = CryptUtil.getAuthorityContent(user.getCredential());
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

        //使用授权式加密方法对密码进行加密
        user.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(user.getPassword()));

        //设置user的相关默认值
        user.setHead("default.jpg");
        user.setCreateTime(new Date());
        user.setDeleted(false);
        user.setRoleId(1);

        //向数据库中注册用户
        int res = userMapper.add(user);
        if (res == 1) {
            return new Result(1,"注册成功",null,null);
        }
        return new Result(2,"注册失败",null,null);
    }

    /**
     * 判断指定的用户名（邮箱）是否已被使用
     * @param user user对象至少包含一个已确定的username属性
     * @return 返回一个Result对象
     * */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED,timeout = 20)
    public Result checkExist(User user) {
        User user1 = userMapper.queryByUsername(user.getUsername());
        if (user1 != null) {
            return new Result(2,"用户已存在",null,null);
        }
        return new Result(1,"",null,null);
    }

    /**
     * 缓存成功登陆的用户详细信息到Redis
     * @param user User对象，至少包含username一个属性
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result cacheLogin(User user) {
        Result result = delegatingCheckUser(user,"cacheLogin");
        if (result != null) {
            return result;
        }

        user = userMapper.queryByUsername(user.getUsername());
        if(user == null) {
            return new Result(2,"用户不存在",null,null);
        }
        //敏感信息不保存
        user.setPassword(null);
        ValueOperations opsForValue = redisTemplate.opsForValue();
        opsForValue.set("login.user." + user.getUsername(), user,30, TimeUnit.MINUTES);
        return new Result(1,"缓存成功",null,null);
    }

    /**
     * 根据业务场景检查User的合法性
     * @param user 要检查的User对象
     * @param context 业务场景
     * @return 不合法合法返回Result对象，合法返回null
     * */
    private Result delegatingCheckUser(User user,String context) {
        Result result = null;
        switch (context) {
            //注册
            case "register" : {
                result = user.checkUsername();
                if (result != null) {
                    result.setStatus(2);
                    return result;
                }
                result = user.checkPassword();
                if (result != null) {
                    result.setStatus(2);
                    return result;
                }
                if (!user.getPassword().equals(user.getPassword1())) {
                    result = new Result(2,"两次密码不一致",null,null);
                    return result;
                }
                result = user.checkName();
                if (result != null) {
                    result.setStatus(2);
                    return result;
                }
                result = user.checkCredential();
                if (result != null) {
                    result.setStatus(2);
                    return result;
                }
                break;
            }
            //检查用户是否已存在
            case "checkExist" : {
                result = user.checkUsername();
                if (result != null){
                    result.setStatus(2);
                    return result;
                }
                break;
            }
            //创建登陆缓存
            case "cacheLogin" : {
                result = user.checkUsername();
                if (result != null){
                    result.setStatus(2);
                    return result;
                }
                break;
            }
            //修改密码
            case "editPassword" : {
                result = user.checkUsername();
                if (result != null) {
                    result.setStatus(2);
                    return result;
                }
                result = user.checkPassword();
                if (result != null) {
                    result.setStatus(2);
                    return result;
                }
                break;
            }
            //修改用户信息（角色，名字）
            case "editUser" : {
                result = user.checkUsername();
                if (result != null) {
                    result.setStatus(2);
                    return result;
                }
                if (user.getName() != null) {
                    result = user.checkName();
                    if (result != null) {
                        result.setStatus(2);
                        return result;
                    }
                }
                if (user.getRoleId() != null) {
                    Role role = roleMapper.queryById(user.getRoleId());
                    if (role == null) {
                        return new Result(2,"该角色不存在",null,null);
                    }
                }
                user.setId(null);
                user.setPassword(null);
                user.setHead(null);
                user.setCreateTime(null);
                user.setDeleted(null);
                break;
            }
        }
        return null;
    }
}
