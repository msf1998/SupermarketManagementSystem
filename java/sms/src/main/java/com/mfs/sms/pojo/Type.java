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
public class Type {
    //基本属性
    private Integer id;
    private String name;
    private Date createTime;
    private Integer parentId;
    private User parent;

    //为实现业务逻辑所需属性
    private Integer page;
    private String order;
}
