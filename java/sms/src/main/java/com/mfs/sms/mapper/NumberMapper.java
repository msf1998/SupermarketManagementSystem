package com.mfs.sms.mapper;

import com.mfs.sms.pojo.Number;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NumberMapper {
    int add(Number number);
    int delete(Number number);
    int update(Number number);
    List<Number> query(Number number);
    Number queryById(String id);
}
