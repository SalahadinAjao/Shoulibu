package org.example.service;

import org.example.entity.SysSmsLogEntity;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/5/30 下午5:25
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface SysSmsLogService {

    //根据id主键查询
    SysSmsLogEntity queryObject(String id);
    //列表查询
    List<SysSmsLogEntity> queryList(Map<String,Object> map);
    //查询总数
    int queryTotal(Map<String,Object> map);

    int save(SysSmsLogEntity entity);

    int update(SysSmsLogEntity entity);

    int delete(String id);

    int deleteBatch(String[] ids);

    SysSmsLogEntity sendSms(SysSmsLogEntity entity);

}
