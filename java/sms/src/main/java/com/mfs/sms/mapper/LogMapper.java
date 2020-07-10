package com.mfs.sms.mapper;

import com.mfs.sms.pojo.Log;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper {
    int add(Log log);
}
