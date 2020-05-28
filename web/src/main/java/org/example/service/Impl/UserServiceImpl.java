package org.example.service.Impl;

import org.example.Util.R;
import org.example.dao.UserDao;
import org.example.entity.UserEntity;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: houlintao
 * @Date:2020/5/18 上午4:47
 * @email 437547058@qq.com
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Override
    public int save(UserEntity entity) {
        userDao.save(entity);
        return 200;
    }

    @Override
    public List<UserEntity> queryAll() {
        return userDao.queryAll();
    }

    @Override
    public R update(UserEntity entity) {
        userDao.update(entity);
        return R.ok();
    }
}
