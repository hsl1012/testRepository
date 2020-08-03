package com.fh.member.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.common.ServerResponse;
import com.fh.member.mapper.MemberMapper;
import com.fh.member.model.Member;
import com.fh.util.JwtUtil;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    @Resource
    protected MemberMapper memberMapper;

    @Override
    //判断用户名是否存在  注册
    public ServerResponse checkMemberName(String name) {
        //用mybatis-plus
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        Member member = memberMapper.selectOne(queryWrapper);
        if(member==null){
            return ServerResponse.success();
        }else {
            return ServerResponse.error("该用户已存在");
        }

    }

    @Override
    //手机号
    public ServerResponse checkMemberPhone(String phone) {
        //用mybatis-plus
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        Member member = memberMapper.selectOne(queryWrapper);
        if(member==null){
            return ServerResponse.success();
        }else {
            return ServerResponse.error("该手机号已被注册");
        }
    }

    @Override
    //验证码
    public ServerResponse redister(Member member) {
        String redisCode = RedisUtil.get(member.getCode());
        if (redisCode==null){
            return  ServerResponse.error("验证码失效");
        }
        if (!redisCode.equals(member.getCode())){
            return  ServerResponse.error("验证码不正确");
        }
        memberMapper.insert(member);
        return ServerResponse.success();
    }

    @Override
    //注册
    public ServerResponse register(Member member) {
       String code = RedisUtil.get(member.getPhone());
       if (!String.valueOf(member.getCode()).equals(code)){
           return ServerResponse.error("验证码错误");
       }
        memberMapper.insert(member);
        return ServerResponse.success();
    }

    @Override
    //登录
    public ServerResponse login(Member member) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        //比较用户名是否正确
        queryWrapper.eq("name",member.getName());
        //或者
        queryWrapper.or();
        //比较手机号是否正确
        queryWrapper.eq("phone",member.getPhone());
        Member memberDB = memberMapper.selectOne(queryWrapper);
        if (memberDB==null){
            return ServerResponse.error("用户名或者手机号不存在");
        }
        //判断密码
        if (!member.getPassword().equals(memberDB.getPassword())){
            return ServerResponse.error("密码不正确");
        }
        //账号密码正确   生成tocken 返回前台
        String token =null;
        try {
            //转换jsonstring形式
            String jsonString = JSONObject.toJSONString(memberDB);
            //编译格式
            String encodeJson = URLEncoder.encode(jsonString, "utf-8");
            //生成token
            token = JwtUtil.sign(encodeJson);

            //设置token失效时间
            RedisUtil.setex(SystemConstant.TOKEN_KEY+token,token, SystemConstant.TOKEN_EXPIRE_TIME);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //返回给前台
        return ServerResponse.success(token);
    }

    @Override
    //下单时查询订单的地址
    public ServerResponse queryMemberList() {
        QueryWrapper<Member> queryWrapper =new QueryWrapper<>();
        List<Member> list = memberMapper.selectList(queryWrapper);
        return ServerResponse.success(list);
    }
}
