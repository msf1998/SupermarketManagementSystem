package com.mfs.sms.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Comparable<Role>{
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

    @Override
    public int compareTo(Role r) {
        if (productInsert == true && r.getProductInsert() == false) {
            return 1;
        }
        if (productDelete == true && r.getProductDelete() == false) {
            return 1;
        }
        if (productUpdate == true && r.getProductUpdate() == false) {
            return 1;
        }
        if (productSelect == true && r.getProductSelect() == false) {
            return 1;
        }
        if (typeInsert == true && r.getTypeInsert() == false) {
            return 1;
        }
        if (typeDelete == true && r.getTypeDelete() == false) {
            return 1;
        }
        if (typeUpdate == true && r.getTypeUpdate() == false) {
            return 1;
        }
        if (typeSelect == true && r.getTypeSelect() == false) {
            return 1;
        }
        if (numberInsert == true && r.getNumberInsert() == false) {
            return 1;
        }
        if (numberDelete == true && r.getNumberDelete() == false) {
            return 1;
        }
        if (numberUpdate == true && r.getNumberUpdate() == false) {
            return 1;
        }
        if (numberSelect == true && r.getNumberSelect() == false) {
            return 1;
        }
        if (orderInsert == true && r.getOrderInsert() == false) {
            return 1;
        }
        if (orderDelete == true && r.getOrderDelete() == false) {
            return 1;
        }
        if (orderUpdate == true && r.getOrderUpdate() == false) {
            return 1;
        }
        if (orderSelect == true && r.getOrderSelect() == false) {
            return 1;
        }
        if (userInsert == true && r.getUserInsert() == false) {
            return 1;
        }
        if (userDelete == true && r.getUserDelete() == false) {
            return 1;
        }
        if (userUpdate == true && r.getUserUpdate() == false) {
            return 1;
        }
        if (userSelect == true && r.getUserSelect() == false) {
            return 1;
        }
        if (roleInsert == true && r.getRoleInsert() == false) {
            return 1;
        }
        if (roleDelete == true && r.getRoleDelete() == false) {
            return 1;
        }
        if (roleUpdate == true && r.getRoleUpdate() == false) {
            return 1;
        }
        if (roleSelect == true && r.getRoleSelect() == false) {
            return 1;
        }
        return 0;
    }
}
