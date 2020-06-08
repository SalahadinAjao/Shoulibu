package org.example.dao;

import org.apache.ibatis.annotations.Param;
import org.example.entity.SmsLogEntity;
import org.example.entity.UserEntity;

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
