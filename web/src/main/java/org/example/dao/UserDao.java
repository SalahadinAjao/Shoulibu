package org.example.dao;

import org.example.entity.UserEntity;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/5/18 上午4:48
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface UserDao extends BaseDao<UserEntity> {

    List<UserEntity> queryAll();
}
