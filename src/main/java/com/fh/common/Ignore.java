package com.fh.common;

import java.lang.annotation.*;

@Documented    //声明它是注解
@Target(ElementType.METHOD)  //用于描述注解的使用范围 用在方法上面
@Retention(RetentionPolicy.RUNTIME)   //定义了该注解被保留的时间长短
public @interface Ignore {
}
