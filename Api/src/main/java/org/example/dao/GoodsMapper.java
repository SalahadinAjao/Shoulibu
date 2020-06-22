package org.example.dao;

import org.example.entity.GoodsEntity;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/21 上午11:43
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface GoodsMapper extends BaseDao<GoodsEntity> {

    List<GoodsEntity> queryHotGoodsList(Map<String, Object> params);

    List<GoodsEntity> queryCatalogProductList(Map<String, Object> params);

}
