package org.example.service;

import org.example.entity.OrderGoodsEntity;
import org.example.dao.OrderGoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/7 上午9:29
 * @email 437547058@qq.com
 * @Version 1.0
 */
@Service
public class OrderGoodsService {
    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    public OrderGoodsEntity queryObject(Integer id){
        return orderGoodsMapper.queryObject(id);
    }

    public List<OrderGoodsEntity> queryList(Map<String,Object> map){
        return orderGoodsMapper.queryList(map);
    }

    public int queryTotal(Map<String,Object> map){
        return orderGoodsMapper.queryTotal(map);
    }

    public void save(OrderGoodsEntity entity){
        orderGoodsMapper.save(entity);
    }

    public void update(OrderGoodsEntity entity){
        orderGoodsMapper.update(entity);
    }

    public void delete(Integer id){
        orderGoodsMapper.delete(id);
    }
    public void deleteBatch(Integer[] ids){
        orderGoodsMapper.deleteBatch(ids);
    }
}
