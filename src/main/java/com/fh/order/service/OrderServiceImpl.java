package com.fh.order.service;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.common.ServerResponse;
import com.fh.member.model.Member;
import com.fh.order.mapper.OrderInFoMapper;
import com.fh.order.mapper.OrderMapper;
import com.fh.order.model.Order;
import com.fh.order.model.OrderInFo;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import com.fh.util.BigDecimalUtil;
import com.fh.util.IdUtil;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper    orderMapper;

     @Resource
    private OrderInFoMapper orderInFoMapper;

     @Resource
    private ProductService productService;

    @Override
    @Transactional
    public ServerResponse buildOrder(List<Cart> cartList, Integer addressId, Integer payType, Member member) {

        //使用雪花算法生成订单编号（缺点 在同一时间可能会相等 几率很小   生成的id为String类型实体类中也是String）
        String orderId = IdUtil.createId();

        List<OrderInFo>  orderInFoList=new ArrayList();
        //商品总价格
        BigDecimal totalPrice=new BigDecimal(0.00);

        //库存不足的集合
        List<String>  stockNotFull =new ArrayList<>();
        for (Cart cart : cartList) {
            //获取商品对象
            Product product = productService.selectProductById(cart.getProductId());
            //商品中的库存如果小于购物车的数量
            if (product.getStock()<cart.getCount()){
               //库存不足
                stockNotFull.add(cart.getName());
            }else{

            //减库存  判断库存是否充足
           Long res= productService.updateStock(product.getId(),cart.getCount());
            if(res==1){
                //库存充足 生成订单详情  调用（下面）方法
                OrderInFo orderInFo = buildOrderInfo(orderId, cart);

                //添加到订单详情里面 吧对象放入集合中
                orderInFoList.add(orderInFo);
                BigDecimal subTotal=BigDecimalUtil.mul(cart.getPrice().toString(),cart.getCount()+"");
                totalPrice=BigDecimalUtil.add(totalPrice,subTotal);

            }else{
                //库存不足
                stockNotFull.add(cart.getName());
            }
            }
        }
        //生成订单 先判断是否有订单详情  订单详情不等于空切订单详情的商品长度等于购物车里的商品长度
        if(orderInFoList!=null && orderInFoList.size()==cartList.size()){
            //库存充足 保存订单详情
            for (OrderInFo orderInFo : orderInFoList) {
                orderInFoMapper.insert(orderInFo);
                //更新购物车 调用（下面）的方法
                updateRedisCart(member, orderInFo);
            }
            //调用（下面）订单的方法
            buildOrder(addressId, payType, member, orderId, totalPrice);
            //返回订单的叮单号
            return  ServerResponse.success(orderId);
        }else {
            return ServerResponse.error(stockNotFull);
        }


    }

    //生成订单详情
    private OrderInFo buildOrderInfo(String orderId, Cart cart) {
        //生成订单详情
        OrderInFo orderInFo=new OrderInFo();
        orderInFo.setProductId(cart.getProductId());
        orderInFo.setName(cart.getName());
        orderInFo.setFilePath(cart.getFilePath());
        orderInFo.setPrice(cart.getPrice());
        orderInFo.setCount(cart.getCount());
        //生成订单编号
        orderInFo.setOrderId(orderId);
        return orderInFo;
    }

    //订单方法
    private void buildOrder(Integer addressId, Integer payType, Member member, String orderId, BigDecimal totalPrice) {
        // 生成订单
        Order order=new Order();
        order.setCreateDate(new Date());
        order.setPayType(payType);
        order.setAddressId(addressId);
        order.setId(orderId);
        order.setTotalPrice(totalPrice);
        order.setUserId(member.getId());
        order.setStatus(SystemConstant.ORDER_STATUS_WAIT);
        orderMapper.insert(order);
    }

    //更新购物车
    private void updateRedisCart(Member member, OrderInFo orderInFo) {
        String cartJSON = RedisUtil.hget(SystemConstant.CART_KEY+member.getId(), orderInFo.getProductId().toString());
        if (StringUtils.isNotEmpty(cartJSON)){
            Cart cart1 = JSONObject.parseObject(cartJSON, Cart.class);
            if (cart1.getCount()<=orderInFo.getCount()){
                //删除支付之后的 购物车中该商品
                RedisUtil.hdel(SystemConstant.CART_KEY+member.getId(),orderInFo.getProductId().toString());
            }else {
                //更新购物车
                cart1.setCount(cart1.getCount()-orderInFo.getCount());
                String s = JSONObject.toJSONString(cart1);
               RedisUtil.hset(SystemConstant.CART_KEY + member.getId(), orderInFo.getProductId() + toString(),s);
            }
        }
    }
}
