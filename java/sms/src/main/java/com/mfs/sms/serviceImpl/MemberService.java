package com.mfs.sms.serviceImpl;

import com.mfs.sms.mapper.MemberMapper;
import com.mfs.sms.mapper.UserMapper;
import com.mfs.sms.pojo.Member;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.pojo.User;
import com.mfs.sms.utils.CryptUtil;
import com.mfs.sms.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class MemberService {
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private UserMapper userMapper;

    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 5)
    public Result addNumber(Member member, HttpServletRequest request){
        //用户验证
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryByUsername(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        //权限验证
        if (!user.getRole().getNumberInsert()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        //查看是否已注册
        Member member2 = memberMapper.queryById(member.getPhone());
        System.out.println(member2);
        if (member2 != null) {
            if (member2.getDeleted() == true) {
                member.setDeleted(false);
                member.setId(member.getPhone());
                member.setCreateTime(new Date());
                member.setParentId(userId);
                member.setScore(0.0);
                int res = memberMapper.update(member);
                if (res == 1) {
                    Member member1 = new Member();
                    member1.setDeleted(false);
                    member1.setPage(0);
                    member1.setOrder("name");
                    List<Member> list = memberMapper.query(member1);
                    return new Result(1,"添加成功",list, CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
                } else {
                    return new Result(2,"添加失败",null,null);
                }
            }
            return new Result(2,"该手机号已注册",null,null);
        }
        member.setId(member.getPhone());
        member.setCreateTime(new Date());
        member.setParentId(userId);
        member.setScore(0.0);
        member.setDeleted(false);
        int res = memberMapper.add(member);
        if (res == 1) {
            Member member1 = new Member();
            member1.setDeleted(false);
            member1.setPage(0);
            member1.setOrder("name");
            List<Member> list = memberMapper.query(member1);
            return new Result(1,"添加成功",list, CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"添加失败",null,null);
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result deleteNumber(Member member, HttpServletRequest request){
        //用户验证
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryByUsername(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        //权限验证
        if (!user.getRole().getNumberDelete()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        if (member.getId().equals("1")) {
            return new Result(2,"改会员不允许删除",null,null);
        }
        member.setDeleted(true);
        int res = memberMapper.update(member);
        if (res == 1) {
            Member member1 = new Member();
            member1.setPage(0);
            member1.setOrder("name");
            member1.setDeleted(false);
            List<Member> list = memberMapper.query(member1);
            return new Result(1,"删除成功",list, CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"删除失败",null,null);
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result editNumber(Member member, HttpServletRequest request){
        //用户验证
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryByUsername(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        //权限验证
        if (!user.getRole().getNumberUpdate()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        int res = memberMapper.update(member);
        if (res == 1) {
            Member member1 = new Member();
            member1.setPage(0);
            member1.setOrder("name");
            member1.setDeleted(false);
            List<Member> list = memberMapper.query(member1);
            return new Result(1,"修改成功",list, CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"修改失败",null,null);
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result listNumber(Member member, HttpServletRequest request){
        //用户验证
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryByUsername(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        //权限验证
        if (!user.getRole().getNumberSelect()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        member.setDeleted(false);
        member.setPage(member.getPage() * 10);
        List<Member> list = memberMapper.query(member);
        return new Result(1,"查询成功",list, CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
    }
}
