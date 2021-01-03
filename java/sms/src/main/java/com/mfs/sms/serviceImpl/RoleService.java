package com.mfs.sms.serviceImpl;

import com.mfs.sms.mapper.RoleMapper;
import com.mfs.sms.mapper.UserMapper;
import com.mfs.sms.pojo.*;
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
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @Transactional(isolation = Isolation.DEFAULT,timeout = 5)
    public Result addRole(Role role, HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryByUsername(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getRoleInsert()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        role.setCreateTime(new Date());
        role.setParentId(userId);
        if (role.getProductInsert()) {
            role.setTypeSelect(true);
        }
        if (role.getUserInsert()) {
            role.setRoleSelect(true);
        }
        int res = roleMapper.add(role);
        if (res == 1) {
            Role role1 = new Role();
            role1.setOrder("id");
            role1.setPage(0);
            List<Role> list = roleMapper.query(role1);
            //更新token
            return new Result(1,"添加成功",list, CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"添加失败",null,null);
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result deleteRole(Role role,HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil .getUserId(request);
        User user = userMapper.queryByUsername(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        //权限验证
        if (!user.getRole().getRoleDelete()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        //查看该角色是否正在被使用
        User user1 = new User();
        user1.setRoleId(role.getId());
        List<User> list1 = userMapper.query(user1);
        if (!(list1 == null || list1.size() == 0)) {
            return new Result(2,"该角色正在被"+list1.size()+"个员工使用",null,null);
        }
        if (role.getId() == 1 || role.getId() == 2) {
            return new Result(2,"不能删除默认角色",null,null);
        }

        int res = roleMapper.delete(role);
        if (res == 1) {
            Role role1 = new Role();
            role1.setOrder("id");
            role1.setPage(0);
            List<Role> list = roleMapper.query(role1);
            return new Result(1,"删除成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"删除失败",null,null);
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result editRole(Role role, HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryByUsername(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getRoleUpdate()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        if (role.getProductInsert()) {
            role.setTypeSelect(true);
        }
        if (role.getUserInsert()) {
            role.setRoleSelect(true);
        }
        int res = roleMapper.update(role);
        if (res == 1) {
            Role role1 = new Role();
            role1.setOrder("id");
            role1.setPage(0);
            List<Role> list = roleMapper.query(role1);
            return new Result(1,"修改成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"修改失败",null,null);
        }
    }

    /**
     * 获取角色列表
     * @param principal 已登录的用户主体
     * @param role 查询参数
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result listRole(Principal principal, Role role) {
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getRoleSelect()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        if (role.getPage() != null) {
            role.setPage(role.getPage() * 10);
        }
        List<Role> list = roleMapper.query(role);
        return new Result(1,"查询成功",list,null);
    }
}
