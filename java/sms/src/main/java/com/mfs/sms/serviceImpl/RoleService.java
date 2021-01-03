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
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    /**
     * 添加角色
     * @param principal 已登录的用户主体
     * @param role 要插入的角色信息
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result addRole(Principal principal, Role role) {
        //参数检查
        Result result = delegatingParameterObjectCheck(role, "addRole");
        if (result != null) {
            return result;
        }

        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getRoleInsert()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        //插入
        role.setCreateTime(new Date());
        role.setParentId(user.getId());
        if (role.getProductInsert()) {
            role.setTypeSelect(true);
        }
        if (role.getUserInsert()) {
            role.setRoleSelect(true);
        }
        int res = roleMapper.add(role);
        if (res == 1) {
            return new Result(1,"添加成功",null,null);
        } else {
            return new Result(2,"添加失败",null,null);
        }
    }

    /**
     * 删除角色
     * @param principal 已登录的用户主体
     * @param role 要删除的角色信息
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result deleteRole(Principal principal, Role role) {
        //元素检查
        Result result = delegatingParameterObjectCheck(role, "deleteRole");
        if (result != null) {
            return result;
        }

        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getRoleDelete()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        if (role.getId() == 1 || role.getId() == 2) {
            return new Result(2,"不能删除默认角色",null,null);
        }

        //查看该角色是否正在被使用
        User user1 = new User();
        user1.setRoleId(role.getId());
        List<User> list1 = userMapper.query(user1);
        if (!(list1 == null || list1.size() == 0)) {
            return new Result(2,"该角色正在被"+list1.size()+"个员工使用",null,null);
        }

        int res = roleMapper.delete(role);
        if (res == 1) {
            return new Result(1,"删除成功",null,null);
        } else {
            return new Result(2,"删除失败",null,null);
        }
    }

    /**
     * 修改角色信息
     * @param principal 已登录的用户主体
     * @param role 要修改的角色信息
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result editRole(Principal principal, Role role) {
        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
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
            return new Result(1,"修改成功",null,null);
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

        //分页设置
        if (role.getPage() != null) {
            role.setPage(role.getPage() * 10);
        }
        if (role.getOffset() == null) {
            role.setOffset(10);
        }
        List<Role> list = roleMapper.query(role);
        return new Result(1,"查询成功",list,null);
    }

    /**
     *
     * */
    private Result delegatingParameterObjectCheck(Object object,String context) {
        switch (context) {
            case "addRole" : {
                Role role = (Role) object;
                role.setId(null);
                if (role.getName() == null || role.getName().length() > 32 || role.getName().equals("")) {
                    return new Result(2,"角色名有误",null,null);
                }
                break;
            }
            case "deleteRole" : {
                Role role = (Role) object;
                if (role.getId() == null || role.getId() < 0) {
                    return new Result(2,"角色id不合法",null,null);
                }
                break;
            }
            case "editRole" : {
                Role role = (Role) object;
                if (role.getId() == null || role.getId() < 0) {
                    return new Result(2,"角色id不合法",null,null);
                }
                if (role.getName() != null && (role.equals("") || role.getName().length() > 32)) {
                    return new Result(2,"角色名不合法",null,null);
                }
                if (role.getName() == null && role.getProductInsert() == null && role.getProductDelete() == null &&
                        role.getProductUpdate() == null && role.getProductSelect() == null && role.getTypeInsert() == null
                        && role.getTypeDelete() == null && role.getTypeUpdate() == null && role.getTypeSelect()== null
                        && role.getMemberInsert() == null && role.getMemberDelete() == null && role.getMemberUpdate() == null
                        && role.getMemberSelect() == null && role.getOrderInsert() == null && role.getOrderDelete() == null
                        && role.getOrderUpdate() == null && role.getUserInsert() == null && role.getUserDelete() == null
                        && role.getUserUpdate() == null && role.getUserSelect() == null   && role.getRoleInsert() == null
                        && role.getRoleDelete() == null && role.getRoleUpdate() == null && role.getRoleSelect() == null) {
                    return new Result(2,"修改参数有误",null,null);
                }
                break;
            }
        }
        return null;
    }
}
