package com.mfs.sms.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data   //添加get、set方法
@AllArgsConstructor  //全参构造方法
@NoArgsConstructor   //无参构造方法
@ToString
public class User {

    //用户基本信息
    private String id;
    private String password;
    private String salt;
    private String head;
    private String name;
    private Integer roleId;
    private Role role;
    private Date createTime;
    private Boolean deleted;
    //private Integer position;

    //为实现业务逻辑所需属性
    private String confirmCode;
    private Integer page;
    private String order;
}
