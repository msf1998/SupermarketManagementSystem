package com.mfs.sms.pojo;

import lombok.ToString;

import java.util.Date;

@ToString
public class Log {
    private Integer id;
    private String action;
    private String result;
    private Date createTime;
    private String creator;
    private static Log log;


    private Log(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public static Log getLog() {
        if (log == null) {
            synchronized (Log.class) {
                if (log == null) {
                    log = new Log();
                }
            }
        }
        return log;
    }
}
