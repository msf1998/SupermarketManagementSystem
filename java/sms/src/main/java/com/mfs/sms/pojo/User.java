package com.mfs.sms.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data   //添加get、set方法
@AllArgsConstructor  //全参构造方法
@NoArgsConstructor   //无参构造方法
@ToString
public class User implements Serializable {

    /**
     * 用户基本信息
     * */
    //id：主键，数据库自增
    private int id;
    //账号：邮箱，长度最长32
    private String username;
    //密码：最长255
    private String password;
    //头像：保存图片名称，最长255
    private String head;
    //名称：用户名,最长32
    private String name;
    //角色id
    private Integer roleId;
    //角色
    private Role role;
    //注册时间
    private Date createTime;
    //逻辑删除
    private Boolean deleted;

    /**
     * 为实现业务逻辑所需属性
     * */
    private String password1;
    private String credential;
    private Integer page;
    private Integer offset;
    private String order;

    /**
     * 检查username是否合法
     * @return 不合法返回Result，只填充了describe属性，合法返回null
     * */
    public Result checkUsername() {
        Result result = null;
        if (username == null || username.equals("")) {
            result = new Result();
            result.setDescribe("用户名不能为空");
        } else if (username.length() > 32) {
            result = new Result();
            result.setDescribe("用户名不能长于32位");
        } else if(!username.contains("@")){
            result = new Result();
            result.setDescribe("用户名必须是合法的邮箱地址");
        }
        return result;
    }
    /**
     * 检查password是否合法
     * @return 不合法返回Result，合法返回null
     * */
    public Result checkPassword() {
        Result result = null;
        if (password == null || password.equals("")) {
            result = new Result();
            result.setDescribe("密码不能为空");
        } else if (password.length() > 255) {
            result = new Result();
            result.setDescribe("密码不能长于255位");
        }
        return result;
    }

    /**
     * 检查name是否合法
     * @return 不合法返回Result，合法返回null
     * */
    public Result checkName() {
        Result result = null;
        if (name == null || name.equals("")) {
            result = new Result();
            result.setDescribe("昵称不能为空");
        } else if (name.length() > 32) {
            result = new Result();
            result.setDescribe("昵称不能长于32位");
        }
        return result;
    }

    /**
     * 检查credential是否合法
     * @return 不合法返回Result，合法返回null
     * */
    public Result checkCredential() {
        Result result = null;
        if (credential == null || credential.equals("")) {
            result = new Result();
            result.setDescribe("证书不能为空");
        }
        return result;
    }

}
