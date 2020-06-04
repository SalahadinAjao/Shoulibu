package org.example.service.impl;

import org.example.dao.SysRoleDeptDao;
import org.example.service.SysRoleDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/4 上午10:28
 * @email 437547058@qq.com
 * @Version 1.0
 */

@Service("sysRoleDeptService")
public class SysRoleDeptServiceImpl implements SysRoleDeptService {
    @Autowired
    private SysRoleDeptDao roleDeptDao;

    @Override
    @Transactional
    public void saveOrUpdate(Long roleId, List<Long> deptIdList) {
        int delete = roleDeptDao.delete(roleId);
        if (deptIdList.size() == 0) {
            return;
        }

        //保存角色与菜单关系
        Map<String, Object> map = new HashMap<>();
        map.put("roleId", roleId);
        map.put("deptIdList", deptIdList);
        roleDeptDao.save(map);
    }

    @Override
    public List<Long> queryDeptIdList(Long roleId) {
        return null;
    }

    @Override
    public List<Long> queryDeptIdListByUserId(Long userId) {
        return null;
    }
}
