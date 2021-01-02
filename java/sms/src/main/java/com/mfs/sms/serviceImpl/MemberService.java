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
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class MemberService {
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private UserService userService;

    /**
     * 添加会员
     * @param principal 已登录的用户主体
     * @param member 添加的会员信息
     * @return Result
     * */
    @Transactional(isolation = Isolation.REPEATABLE_READ,timeout = 20)
    public Result addMember(Principal principal, Member member){
        //参数检查
        Result result = delegatingParameterObjectCheck(member, "addMember");
        if (result != null) {
            return result;
        }

        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        //权限验证
        if (!user.getRole().getMemberInsert()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        //查看是否已注册,已经注册但是又被注销的用户采取解除注销的策略
        Member member2 = memberMapper.queryById(member.getPhone());
        //已注册
        if (member2 != null) {
            //已注销
            if (member2.getDeleted() == true) {
                member2.setDeleted(false);
                member2.setCreateTime(new Date());
                member2.setParentId(user.getId());
                member2.setScore(0.0);
                int res = memberMapper.update(member2);
                if (res == 1) {
                    return new Result(1,"添加成功",null,null);
                } else {
                    return new Result(2,"添加失败",null,null);
                }
            }
            return new Result(2,"该手机号已注册",null,null);
        }

        //未注册
        member.setId(member.getPhone());
        member.setCreateTime(new Date());
        member.setParentId(user.getId());
        member.setScore(0.0);
        member.setDeleted(false);
        int res = memberMapper.add(member);
        if (res == 1) {
            return new Result(1,"添加成功",null,null);
        } else {
            return new Result(2,"添加失败",null,null);
        }
    }

    /**
     * 删除会员（通过id）
     * @param principal 已登录的用户主体
     * @param member 要删除的会员信息
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result deleteMember(Principal principal, Member member){
        //参数检查
        Result result = delegatingParameterObjectCheck(member, "deleteMember");
        if (result != null) {
            return result;
        }

        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getMemberDelete()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        //会员1为普通用户不允许删除
        if (member.getId().equals("1")) {
            return new Result(2,"改会员不允许删除",null,null);
        }

        //修改数据库，逻辑删除
        member.setDeleted(true);
        int res = memberMapper.update(member);
        if (res == 1) {
            return new Result(1,"删除成功",null,null);
        } else {
            return new Result(2,"删除失败",null,null);
        }
    }

    /**
     * 修改会员信息
     * @param principal 已登录的用户主体
     * @param member 要修改的会员信息
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result editMember(Principal principal, Member member){
        //参数检查
        Result result = delegatingParameterObjectCheck(member, "editMember");
        if (result != null) {
            return result;
        }

        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getMemberUpdate()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        //修改
        int res = memberMapper.update(member);
        if (res == 1) {
            return new Result(1,"修改成功",null,null);
        } else {
            return new Result(2,"修改失败",null,null);
        }
    }

    /**
     * 获取会员列表
     * @param principal 已登录的用户主体
     * @param member 要修改的会员信息
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result listMember(Principal principal,Member member){
        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getMemberSelect()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        //查询
        member.setDeleted(false);
        member.setPage(member.getPage() * 10);
        List<Member> list = memberMapper.query(member);
        return new Result(1,"查询成功",list, null);
    }

    /**
     * 根据场景进行参数检查
     * @param object 被检查的对象
     * @param context 业务场景
     * @return Result 或 null
     **/
    private Result delegatingParameterObjectCheck(Object object, String context) {
        switch (context) {
            case "addMember" : {
                Member member = (Member) object;
                if (member.getName() == null || member.getName().equals("") || member.getName().length() > 4) {
                    return new Result(2,"会员姓名不合法",null,null);
                }
                if (member.getPhone() == null || member.getName().equals("") || !member.getPhone().matches("[0-9]{11}")) {
                    return new Result(2,"手机号不合法",null,null);
                }
                if (member.getIdNumber() == null || member.getIdNumber().equals("") || !member.getIdNumber().matches("[0-9]{17}([0-9]|x)")) {
                    return new Result(2,"身份证号码不合法",null,null);
                }
                break;
            }
            case "deleteMember" : {
                Member member = (Member) object;
                if (member.getId() == null) {
                    return new Result(2,"会员Id不合法",null,null);
                }
                break;
            }
            case "editMember" : {
                Member member = (Member) object;
                if (member.getId() == null || !member.getId().matches("[0-9]{11}")) {
                    return new Result(2,"用户id不合法",null,null);
                }
                if (member.getPhone() != null && !member.getPhone().matches("[0-9]{11}")) {
                    return new Result(2,"手机号不合法",null,null);
                }
                if (member.getName() != null && member.getName().length() > 4) {
                    return new Result(2,"姓名不合法",null,null);
                }
                member.setDeleted(null);
                member.setScore(null);
                member.setCreateTime(null);
                member.setParentId(null);
                member.setIdNumber(null);
                break;
            }
        }
        return null;
    }
}
