package org.example.service.impl;

import org.example.entity.SysMenuEntity;
import org.example.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/3 下午5:03
 * @email 437547058@qq.com
 * @Version 1.0
 */

@Service("SysMenuService")
public class SysMenuServiceIMpl implements SysMenuService {
    @Override
    public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> userMenuIdList) {
        return null;
    }

    @Override
    public List<SysMenuEntity> queryNotButtonList() {
        return null;
    }

    @Override
    public List<SysMenuEntity> getUserMenuList(Long userId) {
        return null;
    }

    @Override
    public SysMenuEntity queryObject(Long menuId) {
        return null;
    }

    @Override
    public List<SysMenuEntity> queryList(Map<String, Object> map) {
        return null;
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return 0;
    }

    @Override
    public void save(SysMenuEntity menu) {

    }

    @Override
    public void update(SysMenuEntity menu) {

    }

    @Override
    public void deleteBatch(Long[] menuIds) {

    }

    @Override
    public List<SysMenuEntity> queryUserPermList(Long userId) {
        return null;
    }
}
