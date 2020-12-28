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
public class Order {
    private Integer id;
    private String describe;
    private Double sum;
    private Date createTime;
    private String creatorId;
    private Member creator;
    private String parentId;
    private User parent;

    private String order;
    private Integer page;
}
