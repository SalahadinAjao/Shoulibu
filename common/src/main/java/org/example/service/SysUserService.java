package org.example.service;

import org.example.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/5/31 下午6:35
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface SysUserService {

    /**
     * 查询用户列表
     */
    List<SysUserEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    //更新密码
    /**
     *@date: 2020/6/1 下午6:39
     *@param userId 用户id
     *@param oldPass 旧密码
     *@param newPass 新密码
     *@return:
     *@Description:修改密码
     */
    int updatePassword(Long userId, String oldPass, String newPass);
    //通过用户id查询用户实体
    SysUserEntity queryObject(Long userId);

    void save(SysUserEntity user);

    void update(SysUserEntity user);

    void deleteBatch(Long[] userIds);
}
