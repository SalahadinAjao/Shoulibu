package org.example.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.util.Assert;
import org.example.Entity.SmsLogEntity;
import org.example.Entity.UserEntity;
import org.example.Util.RRException;
import org.example.dao.ApiUserLevelMapper;
import org.example.dao.ApiUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/5/28 下午6:27
 * @email 437547058@qq.com
 * @Version 1.0
 */
@Service
public class ApiUserService {

    @Autowired
    private ApiUserMapper userMapper;
    @Autowired
    private ApiUserLevelMapper userLevelMapper;

    public UserEntity queryObject(Long userId){
        return userMapper.queryObject(userId);
    }

    public UserEntity queryByOPenId(String openId){
        return userMapper.queryByOpenId(openId);
    }

    public List<UserEntity> queryList(Map<String,Object> map){
        return userMapper.queryList(map);
    }

    public int queryTotal(Map<String,Object> map){
        return userMapper.queryTotal(map);
    }

    public void save(String mobile,String password){
        UserEntity userEntity = new UserEntity();
        userEntity.setMobile(mobile);
        /**保存密码的时候需要加密，不能保存明文*/
        userEntity.setPassword(DigestUtils.sha256Hex(password));
        userEntity.setUsername(mobile);
        userEntity.setRegister_time(new Date());

        userMapper.save(userEntity);
    }

    public void save(UserEntity entity){
        userMapper.save(entity);
    }

    public void update(UserEntity userEntity){
        userMapper.update(userEntity);
    }

    public void delete(Long userId){
        userMapper.delete(userId);
    }

    public void deleteBatch(Long[] userIds){
        userMapper.deleteBatch(userIds);
    }

    public UserEntity queryByMobile(String mobile){
        return userMapper.queryByMobile(mobile);
    }

    /**
     *@date: 2020/5/29 上午6:16
     *@param:
     *@return:
     *@Description:
     */
    public long login(String mobile,String password){
        UserEntity userEntity = queryByMobile(mobile);

        Assert.isNull(userEntity,"手机号或密码错误");

        if (!userEntity.getPassword().equals(DigestUtils.sha256Hex(password))){
            throw new RRException("手机号或密码错误");
        }
        return userEntity.getUserId();
    }

    public SmsLogEntity querySmsCodeByUserId(Long userId){
        return userMapper.querySmsCodeByUserId(userId);
    }

    public int saveSmsCodeLog(SmsLogEntity entity){
        return userMapper.saveSmsCodeLog(entity);
    }

}
