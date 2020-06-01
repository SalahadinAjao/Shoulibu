package org.example.service.impl;

import org.example.dao.SysUserRoleDao;
import org.example.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: houlintao
 * @Date:2020/6/1 上午6:10
 * @email 437547058@qq.com
 * @Version 1.0
 */

@Service("sysUserRoleService")
public class SysUserRoleServiceImpl implements SysUserRoleService {
    @Autowired
    private SysUserRoleDao sysUserRoleDao;


    @Override
    public void saveOrUpdate(Long userId, List<Long> roleIdList) {
        if (roleIdList.size() == 0){
            return;
        }
        /**
         * 这里为什么要调用delete方法先删除原来的用户与角色对应关系再新建一个角色用户对应关系？
         * 直接update不是更好？
         * 其实还是因为直接delete后再新建保存更省事，因为roleIdLise是一个集合，直接更新它略麻烦
         */
        sysUserRoleDao.delete(userId);

        HashMap<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("roleIdList",roleIdList);

        sysUserRoleDao.save(map);
    }

    @Override
    public List<Long> queryRoleIdList(Long userId) {

        return sysUserRoleDao.queryRoleIdList(userId);
    }

    @Override
    public void delete(Long userId) {
         sysUserRoleDao.delete(userId);
    }
}
