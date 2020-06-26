package org.example.dao;

import org.example.entity.CouponEntity;

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
     * 按类型查询,发放方式 0：按订单发放 1：按用户发放 2:商品转发送券 3：按商品发放 4:新用户注册 5：线下发放 6评价好评红包（固定或随机红包） 7包邮
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
