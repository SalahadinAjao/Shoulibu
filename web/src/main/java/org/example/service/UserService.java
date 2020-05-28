package org.example.service;

import org.example.Util.R;
import org.example.entity.UserEntity;

import java.util.List;

/**
 * @Author: houlintao
 * @Date:2020/5/18 上午4:45
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface UserService {
    //向数据库保存实体数据
    int save(UserEntity entity);

    //全部
    List<UserEntity> queryAll();

    R update(UserEntity entity);
}
