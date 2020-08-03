package com.fh.inteceptor;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.Ignore;
import com.fh.common.LoginException;
import com.fh.common.MyException;
import com.fh.member.model.Member;
import com.fh.util.JwtUtil;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.URLDecoder;

public class LoginInteceptor  implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //处理客户端传过来的自定义信息
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"x-auth,mtoken,content-type");
        //处理客户端发过来 put,delete
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,"PUT.POST,DELETE,GET");


        HandlerMethod  handlerMethod =(HandlerMethod)handler;
        Method method = handlerMethod.getMethod();

        //判断该方法是否需要拦截
        if (method.isAnnotationPresent(Ignore.class)){
            return  true;
        }
        //获取请求头里面的token
        String token = request.getHeader("x-auth");
        //判断有没token 如果没有返回登录页面
        if (StringUtils.isEmpty(token)){
            //自己定义的异常 返回前端状态码 跳转登录页面
            throw new LoginException();
        }

        //吧token放入session中
        request.getSession().setAttribute(SystemConstant.TOKEN_KEY,token);
        //验证token是否失效
        Boolean exists = RedisUtil.exists(SystemConstant.TOKEN_KEY+token);
        if (!exists){
            //token失效
            throw new MyException();
        }

        //验证token
        boolean res = JwtUtil.verify(token);
        if (res){
            //获取token
            String userString = JwtUtil.getUser(token);
            //将token解码 并编译
            String jsonUser = URLDecoder.decode(userString, "utf-8");
            //将json格式转换成实体类对象
            Member member = JSONObject.parseObject(jsonUser, Member.class);
            //吧用户信息放入session
            request.getSession().setAttribute(SystemConstant.SESSION_KEY,member);


        }else {
            throw new LoginException();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
