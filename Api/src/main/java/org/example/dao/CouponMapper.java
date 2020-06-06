package org.example.dao;

import org.example.Entity.CouponEntity;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/5 下午5:34
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface CouponMapper extends BaseDao<CouponEntity> {

    List<CouponEntity> queryUserCoupons(Map<String, Object> params);

    /**
     * 按条件查询用户优惠券
     */
    CouponEntity getUserCoupon(Integer id);

    /**
     * 按类型查询
     *
     * @param params
     * @return
     */
    CouponEntity queryMaxUserEnableCoupon(Map<String, Object> params);

    /**
     * sendType = 1或4 的优惠券
     *
     * @param params
     * @return
     */
    List<CouponEntity> queryUserCouponList(Map<String, Object> params);

    int updateUserCoupon(CouponEntity couponEntity);
}
