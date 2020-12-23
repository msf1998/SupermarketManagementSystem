package com.mfs.sms.mapper;

import com.mfs.sms.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    int add(User user);
    int delete(User user);
    int update(User user);
    List<User> query(User user);
    User queryByUsername(String id);
}
