package com.mfs.sms.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer status;   //状态码 1：操作成功，2：业务失败，3：服务器故障，4：登录问题，5：权限问题
    private String describe;  //状态描述
    private Object object;    //返回数据
    private String token;     //暂时保留
}
