package org.example.dao;

import org.example.entity.SysUserEntity;

import java.util.List;

/**
 * @Author: houlintao
 * @Date:2020/5/27 下午6:27
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface SysUserDao extends BaseDao<SysUserEntity> {

    /**
     *@date: 2020/5/28 上午6:57
     *@param:用户id
     *@return:
     *@Description:根据用户id查询权限
     */
    List<String> queryALlPerms(Long userId);
    /**
     *@date: 2020/5/28 上午6:59
     *@param:
     *@return:
     *@Description:根据用户id查询其可操作的所有菜单id
     */
    List<Long> queryAllMenuId(Long userId);
    /**
     *@date: 2020/5/28 上午7:00
     *@param:
     *@return:
     *@Description:根据用户名查询所对应的用户实体
     */
    SysUserEntity queryByName(String username);


}
