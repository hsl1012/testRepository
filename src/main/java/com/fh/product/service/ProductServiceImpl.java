package com.fh.product.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.common.ServerResponse;
import com.fh.product.mapper.ProductMapper;
import com.fh.product.model.Product;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper  productMapper;


    @Override
    //查询热销的
    public ServerResponse queryisHotProductList() {

        QueryWrapper<Product>  queryWrapper =new QueryWrapper();
        queryWrapper.eq("isHot",1);
        List<Product> list = productMapper.selectList(queryWrapper);
        return ServerResponse.success(list);
    }

    //查询所有商品（侧面导航栏 吧商品分为三份）
    @Override
    public ServerResponse queryProductList() {

        List<Product> list = productMapper.selectList(null);
        return ServerResponse.success(list);
    }

    //分页
    @Override
    public ServerResponse queryProductListPage(long currentPage,long pageSize) {
        //起始位置
        long start =(currentPage-1)*pageSize;

        //查询总条数
        long totalCount =productMapper.queryTotalCount();

        List<Product> list =productMapper.queryList(start,pageSize);
        long totalPage=totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1;



        Map map=new HashMap<>();
        map.put("list",list);
        map.put("totalPage",totalPage);

        return ServerResponse.success(map);
    }

    @Override
    //添加购物车时 判断是否有该商品
    public Product selectProductById(Integer productId) {

        Product product = productMapper.selectById(productId);
        return product;
    }

    //订单支付时库存
    @Override
    public Long updateStock(Integer id, int count) {
        return productMapper.updateStock(id,count);
    }
}
