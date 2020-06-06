package org.example.dao;

import org.apache.ibatis.annotations.Param;
import org.example.Entity.OrderEntity;

/**
 * @Author: houlintao
 * @Date:2020/6/5 下午5:30
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface OrderMapper extends BaseDao<OrderEntity> {

    OrderEntity queryObjectByOrderSn(@Param("orderSn") String order_sn);
}
