package com.mfs.sms.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {
    //基本商品信息
    private String id;
    private String name;
    private String photo;
    private String manufacturer;   //供货商
    private Date productDate;      //生产日期
    private Integer selfLife;      //保质期
    private Integer warnBefore;    //提醒时间，在过期前warnBefore天提醒
    private Integer count;
    private Integer warnCount;     //库存提醒值，在库存小于等于warnCount时提醒库存不足
    private Date inTime;           //入库时间
    private Double inPrice;
    private Double outPrice;
    private String parentId;        //负责人，入库此商品的人
    private User parent;
    private Integer typeId;
    private Type type;
    private String qCode;    //二维码地址

    //为实现业务逻辑所需的属性
    private Integer page;
    private String order;
}
