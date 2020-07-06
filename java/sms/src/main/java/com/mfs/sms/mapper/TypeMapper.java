package com.mfs.sms.mapper;

import com.mfs.sms.pojo.Type;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TypeMapper {
    int add(Type type);
    int delete(Type type);
    int update(Type type);
    List<Type> query(Type type);
    Type queryById(Integer id);
}
