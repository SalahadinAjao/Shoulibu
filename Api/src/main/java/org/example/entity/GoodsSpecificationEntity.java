

package org.example.entity;

import java.io.Serializable;

/**
 * @Author: houlintao
 * @Date:2020/6/21 下午3:26
 * @email 437547058@qq.com
 * @Version 1.0
 * 商品规格实体类
 */
public class GoodsSpecificationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    //主键
    private Integer id;
    //商品
    private Integer goods_id;
    //规格Id
    private Integer specification_id;
    //规格值
    private String value;
    //规格名
    private String name;
    //规格图片
    private String pic_url;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public void setSpecification_id(Integer specification_id) {
        this.specification_id = specification_id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public Integer getId() {
        return id;
    }

    public Integer getGoods_id() {
        return goods_id;
    }

    public Integer getSpecification_id() {
        return specification_id;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getPic_url() {
        return pic_url;
    }
}
