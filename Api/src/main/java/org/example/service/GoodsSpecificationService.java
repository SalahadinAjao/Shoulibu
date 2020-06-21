package org.example.service;

import org.example.dao.GoodsSpecificationMapper;
import org.example.entity.GoodsSpecificationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/21 下午3:30
 * @email 437547058@qq.com
 * @Version 1.0
 */

@Service
public class GoodsSpecificationService {
    @Autowired
    private GoodsSpecificationMapper goodsDao;

    public GoodsSpecificationEntity queryObject(Integer id) {
        return goodsDao.queryObject(id);
    }


    public List<GoodsSpecificationEntity> queryList(Map<String, Object> map) {
        return goodsDao.queryList(map);
    }


    public int queryTotal(Map<String, Object> map) {
        return goodsDao.queryTotal(map);
    }


    public void save(GoodsSpecificationEntity goods) {
        goodsDao.save(goods);
    }


    public void update(GoodsSpecificationEntity goods) {
        goodsDao.update(goods);
    }


    public void delete(Integer id) {
        goodsDao.delete(id);
    }


    public void deleteBatch(Integer[] ids) {
        goodsDao.deleteBatch(ids);
    }
}
