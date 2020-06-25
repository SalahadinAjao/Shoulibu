package org.example.dao;

import org.apache.ibatis.annotations.Param;
import org.example.entity.UserCouponEntity;

/**
 * @Author: houlintao
 * @Date:2020/6/25 上午11:07
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface UserCouponMapper extends BaseDao<UserCouponEntity> {

    UserCouponEntity queryByCouponNumber(@Param("coupon_number") String coupon_number);
}
