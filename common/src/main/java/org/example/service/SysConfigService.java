package org.example.service;

import org.example.entity.SysSmsLogEntity;
import org.example.entity.SysConfigEntity;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/5/30 上午10:00
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface SysConfigService {
    public void save(SysConfigEntity entity);
    public void update(SysConfigEntity entity);
    public void updateValueByKey(String key,String value);
    public void deleteBatch(Long[] ids);
    public List<SysConfigEntity> queryList(Map<String,Object> map);
    public int queryTotal(Map<String,Object> map);

    public SysSmsLogEntity querySmsCodeByUserId(Long userId);
    public SysConfigEntity queryObject(Long id);

    public String getValue(String key, String defaultValue);
    public <T> T getConfigObject(String key, Class<T> clazz);
}
