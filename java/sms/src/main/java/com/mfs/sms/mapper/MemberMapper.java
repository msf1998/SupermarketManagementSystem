package com.mfs.sms.mapper;

import com.mfs.sms.pojo.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    int add(Member member);
    int delete(Member member);
    int update(Member member);
    List<Member> query(Member member);
    Member queryById(String id);
}
