package org.example.service.impl;

import org.example.dao.SysUserDao;
import org.example.entity.SysUserEntity;
import org.example.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/5/31 下午6:35
 * @email 437547058@qq.com
 * @Version 1.0
 */
@Service("sysUserService")
public class SysUserServiceimpl implements SysUserService {

    @Autowired
    private SysUserDao userDao;


    @Override
    public List<SysUserEntity> queryList(Map<String, Object> map) {

        return userDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return userDao.queryTOtal(map);
    }

    @Override
    public int updatePassword(Long userId, String oldPass, String newPass) {
        return 0;
    }

    @Override
    public SysUserEntity queryObject(Long userId) {
        return null;
    }

    @Override
    public void save(SysUserEntity user) {

    }

    @Override
    public void update(SysUserEntity user) {

    }

    @Override
    public void deleteBatch(Long[] userIds) {

    }
}
