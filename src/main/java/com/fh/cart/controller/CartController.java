package com.fh.cart.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.cart.service.CartService;
import com.fh.common.MemberAnnotation;
import com.fh.common.ServerEnum;
import com.fh.common.ServerResponse;
import com.fh.member.model.Member;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("cart")
public class CartController {

    @Resource
    private  CartService  cartService;

    //添加购物车
    @RequestMapping("buy")
    public ServerResponse  buy(Integer productId, Integer count, HttpServletRequest request){
        return cartService.buy(productId,count,request);
    }

    //查询购物车中的数量
    @RequestMapping("queryCartProductCount")
    public ServerResponse  queryCartProductCount(@MemberAnnotation Member member){
        //在session中获取用户信息(上面注解代表request获取session中的用户)
       // Member member = (Member) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        //从redis中获取购物车信息
        List<String> stringList = RedisUtil.hget(SystemConstant.CART_KEY + member.getId());
       long totalCount=0;
        if (stringList!=null && stringList.size()>0){

            for (String str : stringList) {
                Cart cart = JSONObject.parseObject(str, Cart.class);
               //购物车中的数量（新加的数量与之前的 总和）
                totalCount+=cart.getCount();
            }
        }else {
            //如果stringList为空则购物车中的数量为0
            return ServerResponse.success(0);
        }
        return ServerResponse.success(totalCount);
    }


    @RequestMapping("queryList")
    public ServerResponse queryList(@MemberAnnotation Member member){
        List<String> stringList = RedisUtil.hget(SystemConstant.CART_KEY + member.getId());
        List<Cart> cartList=new ArrayList<>();
        if(stringList!=null && stringList.size()>0){
            for (String str : stringList) {
                Cart cart = JSONObject.parseObject(str, Cart.class);
                cartList.add(cart);
            }
        }else {
            return ServerResponse.error(ServerEnum.CART_IS_NULL.getMsg());
        }
        return ServerResponse.success(cartList);
    }

    //删除
     @RequestMapping("deleteProduct/{productId}")
    public ServerResponse deleteProduct(@MemberAnnotation Member member,@PathVariable("productId") Integer productId){

      RedisUtil.hdel(SystemConstant.CART_KEY+member.getId(),productId.toString());

        return ServerResponse.success();
    }


}
