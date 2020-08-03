package com.fh.product.model;

import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@TableName("t_product")
public class Product {

    private Integer   id;
    private String   name;
    private Integer brandId;
    private BigDecimal price;
    private Integer  status;
    private String filePath;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    private Integer categoryId1;
    private Integer categoryId2;
    private Integer categoryId3;
    private Integer isHot;
    private Integer stock;


    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getIsHot() {
        return isHot;
    }

    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }

    public Product() {
    }

    public Product(Integer id, String name, Integer brandId, BigDecimal price, Integer status, String filePath, Date createDate, Integer categoryId1, Integer categoryId2, Integer categoryId3, Integer isHot, Integer stock) {
        this.id = id;
        this.name = name;
        this.brandId = brandId;
        this.price = price;
        this.status = status;
        this.filePath = filePath;
        this.createDate = createDate;
        this.categoryId1 = categoryId1;
        this.categoryId2 = categoryId2;
        this.categoryId3 = categoryId3;
        this.isHot = isHot;
        this.stock = stock;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCategoryId1() {
        return categoryId1;
    }

    public void setCategoryId1(Integer categoryId1) {
        this.categoryId1 = categoryId1;
    }

    public Integer getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(Integer categoryId2) {
        this.categoryId2 = categoryId2;
    }

    public Integer getCategoryId3() {
        return categoryId3;
    }

    public void setCategoryId3(Integer categoryId3) {
        this.categoryId3 = categoryId3;
    }

}
