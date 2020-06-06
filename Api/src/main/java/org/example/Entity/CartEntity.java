package org.example.Entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: houlintao
 * @Date:2020/6/5 下午5:09
 * 用户购物车实体类，封装用户购物车信息
 */
public class CartEntity implements Serializable {
    public static final long serialVersionUID=1L;

    //主键
    private Integer id;
    //用户id
    private Long user_id;
    //sessionId
    private String session_id;
    //商品id
    private Integer goods_id;
    //商品序列号
    private String goods_sn;
    //产品id
    private Integer product_id;
    //产品名称
    private String goods_name;
    //市场价
    private BigDecimal market_price;
    //零售价格
    private BigDecimal retail_price;
    //product表中的零售价格
    private BigDecimal retail_product_price;
    //数量
    private Integer number;
    //规格属性组成的字符串，用来显示用
    private String goods_specifition_name_value;
    //product表对应的goods_specifition_ids
    private String goods_specifition_ids;
    //
    private Integer checked;
    // 节省金额
    private BigDecimal crash_save_price;

    //商品图片
    private String list_pic_url;

    public Integer getId() {
        return id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public Integer getGoods_id() {
        return goods_id;
    }

    public String getGoods_sn() {
        return goods_sn;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public BigDecimal getMarket_price() {
        return market_price;
    }

    public BigDecimal getRetail_price() {
        return retail_price;
    }

    public BigDecimal getRetail_product_price() {
        return retail_product_price;
    }

    public Integer getNumber() {
        return number;
    }

    public String getGoods_specifition_name_value() {
        return goods_specifition_name_value;
    }

    public String getGoods_specifition_ids() {
        return goods_specifition_ids;
    }

    public Integer getChecked() {
        return checked;
    }

    public BigDecimal getCrash_save_price() {
        return crash_save_price;
    }

    public String getList_pic_url() {
        return list_pic_url;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public void setGoods_sn(String goods_sn) {
        this.goods_sn = goods_sn;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public void setMarket_price(BigDecimal market_price) {
        this.market_price = market_price;
    }

    public void setRetail_price(BigDecimal retail_price) {
        this.retail_price = retail_price;
    }

    public void setRetail_product_price(BigDecimal retail_product_price) {
        this.retail_product_price = retail_product_price;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setGoods_specifition_name_value(String goods_specifition_name_value) {
        this.goods_specifition_name_value = goods_specifition_name_value;
    }

    public void setGoods_specifition_ids(String goods_specifition_ids) {
        this.goods_specifition_ids = goods_specifition_ids;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public void setCrash_save_price(BigDecimal crash_save_price) {
        this.crash_save_price = crash_save_price;
    }

    public void setList_pic_url(String list_pic_url) {
        this.list_pic_url = list_pic_url;
    }
}
