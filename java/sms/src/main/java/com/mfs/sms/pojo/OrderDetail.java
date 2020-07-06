package com.mfs.sms.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetail {

    private Integer id;
    private String product;
    private String productName;
    private Double productPrice;
    private Integer count;
    private Integer order;
}
