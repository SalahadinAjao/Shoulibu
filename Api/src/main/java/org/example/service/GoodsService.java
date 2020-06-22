package org.example.service;

import org.example.dao.GoodsMapper;
import org.example.entity.GoodsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/22 下午2:35
 * @email 437547058@qq.com
 * @Version 1.0
 */

@Service
public class GoodsService {
    @Autowired
    private GoodsMapper goodsDao;

    public GoodsEntity queryObject(Integer id){
       return goodsDao.queryObject(id);
    }

    public List<GoodsEntity> queryList(Map<String,Object> map){
        return goodsDao.queryList(map);
    }

    public int queryTotal(Map<String,Object> map){
        return goodsDao.queryTotal(map);
    }

    public void save(GoodsEntity goodsEntity){
        goodsDao.save(goodsEntity);
    }

    public void update(GoodsEntity goodsEntity){
        goodsDao.update(goodsEntity);
    }

    public void delete(Integer id){
        goodsDao.delete(id);
    }
    public void deleteBatch(Integer[] ids) {
        goodsDao.deleteBatch(ids);
    }

    public List<GoodsEntity> queryHotGoodsList(Map<String,Object> map){
       return goodsDao.queryHotGoodsList(map);
    }

    public List<GoodsEntity> queryCatalogProductList(Map<String, Object> map) {
        return goodsDao.queryCatalogProductList(map);
    }
}
