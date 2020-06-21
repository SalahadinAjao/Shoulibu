package org.example.service;

import org.example.dao.CartMapper;
import org.example.entity.CartEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/21 下午4:21
 * @email 437547058@qq.com
 * @Version 1.0
 */
@Service
public class CartService {
    @Autowired
    private CartMapper cartDao;

    private CartEntity queryObject(Integer id){
        return cartDao.queryObject(id);
    }

    private List<CartEntity> queryList(Map<String,Object> map){
        return cartDao.queryList(map);
    }

    private int queryTotal(Map<String,Object> map){
        return cartDao.queryTotal(map);
    }

    public void save(CartEntity cartEntity){

    }
}
