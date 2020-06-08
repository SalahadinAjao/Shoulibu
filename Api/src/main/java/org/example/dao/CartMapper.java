package org.example.dao;

import org.apache.ibatis.annotations.Param;
import org.example.entity.CartEntity;

/**
 * @Author: houlintao
 * @Date:2020/6/5 下午5:32
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface CartMapper extends BaseDao<CartEntity> {

    void updateCheck(@Param("productIds") String[] productIds,
                     @Param("isChecked") Integer isChecked, @Param("userId") Long userId);

    void deleteByProductIds(@Param("productIds") String[] productIds);

    void deleteByUserAndProductIds(@Param("user_id") Long user_id,@Param("productIds") String[] productIds);

    void deleteByCart(@Param("user_id") Long user_id, @Param("session_id") Integer session_id, @Param("checked") Integer checked);
}
