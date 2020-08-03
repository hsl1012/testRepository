package com.fh.cart.service;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.common.ServerEnum;
import com.fh.common.ServerResponse;
import com.fh.member.model.Member;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class CartServiceImpl implements CartService {

   @Autowired
   private ProductService productService;
    @Override
    //加入购物车
    public ServerResponse buy(Integer productId, Integer count, HttpServletRequest request) {
       //1.验证商品是否存在
        Product product=productService.selectProductById(productId);
        if (product==null){
            return ServerResponse.error(ServerEnum.PRODUCT_NOT_EXIST);
        }
        //2.验证商品是否上架
        if (product.getStatus()==0){
            return ServerResponse.error(ServerEnum.PRODUCT_IS_DOWN);
        }


        //3.验证购物车中是否有该商品

        //获取用户的信息   可以知道是哪个用户的购物车
       Member member= (Member) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        boolean exist=RedisUtil.exists(SystemConstant.CART_KEY+member.getId(),productId.toString());
       if(!exist){
           //如果购物车不存在该商品 吧商品放进去
           Cart cart=new Cart();
           cart.setCount(count);
           cart.setFilePath(product.getFilePath());
           cart.setName(product.getName());
           cart.setPrice(product.getPrice());
           cart.setProductId(productId);
            //吧对象转化为json
           String jsonString = JSONObject.toJSONString(cart);
           RedisUtil.hset(SystemConstant.CART_KEY+member.getId(),productId.toString(),jsonString);
       }else {
           //如果存在则修改数量
           String productJson = RedisUtil.hget(SystemConstant.CART_KEY + member.getId(), productId.toString());
           //吧json转化为对象
           Cart cart = JSONObject.parseObject(productJson, Cart.class);
           //修改数量
           cart.setCount(count+cart.getCount());

           //修改完之后 放入redis中
           //吧对象转化为json
           String jsonString = JSONObject.toJSONString(cart);
           RedisUtil.hset(SystemConstant.CART_KEY+member.getId(),productId.toString(),jsonString);
       }

        return ServerResponse.success();
    }
}
