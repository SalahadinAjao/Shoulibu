package org.example.dao;

import org.apache.ibatis.annotations.Param;
import org.example.Entity.SmsLogEntity;
import org.example.Entity.UserEntity;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/5/28 下午6:32
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface ApiUserMapper extends BaseDao<UserEntity> {

    UserEntity queryByMobile(String mobile);

    UserEntity queryByOpenId(@Param("openId") String openId);

    SmsLogEntity querySmsCodeByUserId(Long userId);

    int saveSmsCodeLog(SmsLogEntity entity);


}
