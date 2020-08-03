package com.fh.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.common.Idempotent;
import com.fh.common.MemberAnnotation;
import com.fh.common.ServerResponse;
import com.fh.member.model.Member;
import com.fh.order.service.OrderService;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @RequestMapping("buildOrder") //listStr集合  addressId地区  payType支付方式
    @Idempotent
    public ServerResponse buildOrder(String listStr, Integer addressId, Integer payType,@MemberAnnotation Member member){

        if (StringUtils.isNotEmpty(listStr)){
            List<Cart> cartList = JSONObject.parseArray(listStr, Cart.class);
            return  orderService.buildOrder(cartList,addressId,payType,member);
        }else {
            return ServerResponse.error("请选择商品");
        }

    }

    //幂等性
    @RequestMapping("getToken")
    public  ServerResponse getToken(){
        String mtoken = UUID.randomUUID().toString();
        RedisUtil.set(mtoken,mtoken);
        return ServerResponse.success(mtoken);
    }
}
