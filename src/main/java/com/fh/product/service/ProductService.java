package com.fh.product.service;

import com.fh.common.ServerResponse;
import com.fh.product.model.Product;

import java.util.List;


public interface ProductService {


    ServerResponse queryisHotProductList();

    ServerResponse queryProductList();

    ServerResponse queryProductListPage(long currentPage,long pageSize);

    //添加购物车时 判断是否有该商品
    Product selectProductById(Integer productId);

    Long updateStock(Integer id, int count);
}
