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
    private ProductMapper productMapper;
    @Autowired
    private UserService userService;

    /**
     * 添加新类型
     * @param principal 已登录的用户主体
     * @param type 类型信息
     * @return Result
     **/
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result addType(Principal principal,Type type) {
        //参数检查
        Result result = delegatingParameterObjectCheck(type, "addType");
        if (result != null) {
            return result;
        }

        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getTypeInsert()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        //修改数据库数据
        type.setId(null);
        type.setParentId(user.getId());
        type.setCreateTime(new Date());
        int res = typeMapper.add(type);
        if (res == 1) {
            return new Result(1,"添加成功",null,null);
        } else {
            return new Result(2,"添加失败",null,null);
        }
    }

    /**
     * 删除类型，正在被使用的不能删除
     * @param principal 已登录的
     * @param type 查询参数
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result deleteType(Principal principal,Type type) {
        //参数检查
        Result result = delegatingParameterObjectCheck(type, "deleteType");
        if (result != null) {
            return result;
        }

        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getTypeDelete()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        //查看该类型是否正在被使用
        Product p = new Product();
        p.setTypeId(type.getId());
        List<Product> list = productMapper.query(p);
        if (!(list == null || list.size() == 0)) {
            return new Result(2,"该类型正在被"+list.size()+"个商品使用",null,null);
        }
        int res = typeMapper.delete(type);
        if (res == 1) {
            return new Result(1,"删除成功",null,null);
        } else {
            return new Result(2,"删除失败",null,null);
        }
    }

    /**
     * 修改类型信息
     * @param principal 已登录的用户主体
     * @param type 类型信息
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result editType(Principal principal,Type type) {
        //参数检查
        Result result = delegatingParameterObjectCheck(type, "editType");
        if (result != null) {
            return result;
        }

        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getTypeUpdate()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        //更新数据库数据
        int res = typeMapper.update(type);
        if (res == 1) {
            return new Result(1,"修改成功",null,null);
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

        return new Result(1,"查询成功",list,null);
    }

    /**
     * 根据场景进行参数检查
     * @param object 被检查的对象
     * @param context 业务场景
     * @return Result 或 null
     **/
    private Result delegatingParameterObjectCheck(Object object,String context) {
        switch (context) {
            case "deleteType" : {
                Type type = (Type)object;
                if (type.getId() == null) {
                    return new Result(2,"类型不合法",null,null);
                }
                break;
            }
            case "addType" : {
                Type type = (Type) object;
                if (type.getName() == null || type.getName().length() > 16) {
                    return new Result(2,"类型名称不合法",null,null);
                }
                break;
            }
            case "editType" : {
                Type type = (Type) object;
                if (type.getId() == null) {
                    return new Result(2,"类型不合法",null,null);
                }
                if (type.getName() == null || type.getName().length() > 16) {
                    return new Result(2,"类型名称不合法",null,null);
                }
                break;
            }
        }
        return null;
    }
}
