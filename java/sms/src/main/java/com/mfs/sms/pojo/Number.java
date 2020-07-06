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
public class Number {
    private String id;
    private String name;
    private String phone;
    private String idNumber;
    private Date createTime;
    private Double score;
    private String parentId;
    private User parent;
    private Boolean deleted;

    private Integer page;
    private String order;
}
