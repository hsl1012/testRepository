package com.fh.resolver;

import com.fh.common.MemberAnnotation;
import com.fh.member.model.Member;
import com.fh.util.SystemConstant;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

//参数解析器
@Component
public class MemberResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
       if (parameter.hasParameterAnnotation(MemberAnnotation.class)){
           return true;
       }
        return false;
    }


    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        //吧用户信息放入session中
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Member member = (Member) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        return member;
    }
}
