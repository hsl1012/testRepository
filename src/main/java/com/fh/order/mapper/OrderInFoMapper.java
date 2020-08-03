package com.fh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.order.model.Order;
import com.fh.order.model.OrderInFo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderInFoMapper extends BaseMapper<OrderInFo> {
}
