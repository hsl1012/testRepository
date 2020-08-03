package com.fh.product.controller;

import com.fh.common.Ignore;
import com.fh.common.ServerResponse;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("product")
@RestController
public class ProductController {

    @Resource
    private ProductService productService;

    //查询是否是热销商品
    @Ignore
    @RequestMapping("queryisHotProductList")
    public ServerResponse queryisHotProductList(){

        return productService.queryisHotProductList();
    }

    //查询所有商品
    @Ignore
    @RequestMapping("queryProductList")
    public ServerResponse queryProductList(){

        return productService.queryProductList();
    }

    //查询分页
    @Ignore
    @RequestMapping("queryProductListPage")
    public ServerResponse queryProductListPage(long currentPage,long pageSize){

        return productService.queryProductListPage(currentPage,pageSize);
    }

}
