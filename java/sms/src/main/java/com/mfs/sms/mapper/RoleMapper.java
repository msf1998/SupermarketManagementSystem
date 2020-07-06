package com.mfs.sms.mapper;

import com.mfs.sms.pojo.Product;
import com.mfs.sms.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface RoleMapper {
    int add(Role role);
    int delete(Role role);
    int update(Role role);
    List<Role> query(Role role);
    Product queryById(Integer id);
}
