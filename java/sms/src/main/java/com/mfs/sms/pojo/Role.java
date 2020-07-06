package com.mfs.sms.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private Integer id;
    private String name;
    private Date createTime;
    private String parentId;
    private User parent;
    //对商品的操作权限
    private Boolean productInsert;
    private Boolean productDelete;
    private Boolean productUpdate;
    private Boolean productSelect;
    //对类型的操作权限
    private Boolean typeInsert;
    private Boolean typeDelete;
    private Boolean typeUpdate;
    private Boolean typeSelect;
    //对会员的操作权限
    private Boolean numberInsert;
    private Boolean numberDelete;
    private Boolean numberUpdate;
    private Boolean numberSelect;
    //对订单的操作权限
    private Boolean orderInsert;
    private Boolean orderDelete;
    private Boolean orderUpdate;
    private Boolean orderSelect;
    //对用户的操作权限
    private Boolean userInsert;
    private Boolean userDelete;
    private Boolean userUpdate;
    private Boolean userSelect;
    //对角色的操作权限
    private Boolean roleInsert;
    private Boolean roleDelete;
    private Boolean roleUpdate;
    private Boolean roleSelect;

    //为实现业务逻辑所需的属性
    private Integer page;
    private String order;
}
