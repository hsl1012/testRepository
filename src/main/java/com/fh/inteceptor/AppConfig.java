package com.fh.inteceptor;

import com.fh.resolver.MemberResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class AppConfig extends WebMvcConfigurationSupport {

    //参数解析器
    @Autowired
    public MemberResolver  memberResolver;

    //配置拦截器类
    @Bean
    public  LoginInteceptor loginInteceptor(){
        return new LoginInteceptor();
    }
 //配置幂等性
    @Bean
    public  IdempotentInteceptor idempotentInteceptor(){
        return new IdempotentInteceptor();
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(loginInteceptor()).addPathPatterns("/**");
        //配置幂等性
        registry.addInterceptor(idempotentInteceptor()).addPathPatterns("/**");
    }

    //参数解析器
    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(memberResolver);
    }

    //Swagger接口测试
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        //springboot 集成swagger2.2后静态资源404，添加如下两行配置
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }
}
