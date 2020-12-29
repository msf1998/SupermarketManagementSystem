package com.mfs.sms.mapper;

import com.mfs.sms.pojo.CompareObj;
import com.mfs.sms.pojo.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    int add(Product product);
    int delete(Product product);
    int update(Product product);
    List<Product> query(Product product);
    Product queryById(String id);
    List<Product> queryGreaterThan();
    List<Product> queryBySelfLife();
}
