package org.example.entity;

import java.io.Serializable;

/**
 * @Author: houlintao
 * @Date:2020/6/5 下午8:18
 * 所购买的商品实体类，封装了我们直接购买的商品信息，包括：
 * 商品名，数量，产品id等
 */
public class BuyGoodsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer goodsId;
    private Integer productId;
    private Integer number;
    private String name;

    public Integer getGoodsId() {
        return goodsId;
    }

    public Integer getProductId() {
        return productId;
    }

    public Integer getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }
}
