package org.example.service;

import org.example.dao.UserCouponMapper;
import org.example.entity.UserCouponEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/25 上午11:08
 * @email 437547058@qq.com
 * @Version 1.0
 */
@Service
public class UserCouponService {
    @Autowired
    private UserCouponMapper userCouponMapper;

    public UserCouponEntity queryObject(Integer id) {
        return userCouponMapper.queryObject(id);
    }

    public UserCouponEntity queryByCouponNumber(String couponNumber) {
        return userCouponMapper.queryByCouponNumber(couponNumber);
    }

    public List<UserCouponEntity> queryList(Map<String, Object> map) {
        return userCouponMapper.queryList(map);
    }


    public int queryTotal(Map<String, Object> map) {
        return userCouponMapper.queryTotal(map);
    }


    public void save(UserCouponEntity goods) {
        userCouponMapper.save(goods);
    }


    public void update(UserCouponEntity goods) {
        userCouponMapper.update(goods);
    }


    public void delete(Integer id) {
        userCouponMapper.delete(id);
    }


    public void deleteBatch(Integer[] ids) {
        userCouponMapper.deleteBatch(ids);
    }

}
