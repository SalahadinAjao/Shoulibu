package org.example.dao;

import org.apache.ibatis.annotations.Param;
import org.example.Entity.TokenEntity;

/**
 * @Author: houlintao
 * @Date:2020/5/29 上午6:49
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface TokenMapper extends BaseDao<TokenEntity> {
     TokenEntity queryByUserId(@Param("userId") Long userId);
     TokenEntity queryByToken(@Param("token") String token);
}
