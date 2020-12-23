package com.mfs.sms.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class Log {
    private String action;
    private String result;
    private String creator;
}
