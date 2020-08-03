package com.fh.member.controller;

import com.fh.common.Ignore;
import com.fh.common.ServerResponse;
import com.fh.member.model.Member;
import com.fh.member.service.MemberService;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("member")
public class MemberController {

    @Resource
    private MemberService  memberService;


    //验证用户名是否存在  注册
    @Ignore
    @RequestMapping("checkMemberName")
    public ServerResponse checkMemberName(String name){
       return memberService.checkMemberName(name);
    }

    //验证手机号是否存在  注册
    @Ignore
    @RequestMapping("checkMemberPhone")
    public ServerResponse checkMemberPhone(String phone){
        return memberService.checkMemberPhone(phone);
    }


    //验证码
    @Ignore
    @RequestMapping("redister")
    public ServerResponse  redister(Member member){
    return  memberService.redister(member);
    }

    //注册
    @Ignore
    @RequestMapping("register")
    public  ServerResponse  register(Member member){
        return  memberService.register(member);
    }


    //登录
    @Ignore
    @RequestMapping("login")
    public  ServerResponse  login(Member member){
        return  memberService.login(member);
    }


    //加入购物车之前看用户是否登录
    @RequestMapping("checkLogin")
    public  ServerResponse  checkLogin(HttpServletRequest request){
        //判断用户中是否有值
        Member member = (Member) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        if (member==null){
            return ServerResponse.error();
        }
        return  ServerResponse.success();
    }

    //退出
    @RequestMapping("out")
    @Ignore
    public  ServerResponse  out(HttpServletRequest request){
       //让token失效（用户在有效时间内退出 使token失效 防止别人拿到token去登录）
        String token = (String) request.getSession().getAttribute(SystemConstant.TOKEN_KEY);
        RedisUtil.del(SystemConstant.TOKEN_KEY+token);

        //清除token中的信息       （不删除会查询购物车,显示购物车中的数量）
        request.getSession().removeAttribute(SystemConstant.TOKEN_KEY);
        //清除session用户信息   （不删除会查询购物车,显示购物车中的数、 'aYTDF[POhjkl;poiserty）
        request.getSession().removeAttribute(SystemConstant.SESSION_KEY);
        return  ServerResponse.success();
    }

    //下单时选择（查询）订单地址
    @Ignore
    @RequestMapping("queryMemberList")
    public  ServerResponse  queryMemberList(){
        return  memberService.queryMemberList();
    }
}
