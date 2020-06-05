package org.example.service;

import org.apache.commons.lang.CharUtils;
import org.example.Entity.TokenEntity;
import org.example.Util.CharUtil;
import org.example.dao.TokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/5/29 上午6:55
 * @email 437547058@qq.com
 * @Version 1.0
 */

@Service
public class TokenService {
    @Autowired
    private TokenMapper tokenMapper;

    //设置token过期时间为距离创建时间往后30天
    private static final Long FINALTIME=3600*24*30L;

    public TokenEntity queryByUserId(Long userId) {
        return tokenMapper.queryByUserId(userId);
    }

    public TokenEntity queryByToken(String token) {
        return tokenMapper.queryByToken(token);
    }

    public void save(TokenEntity token) {
        tokenMapper.save(token);
    }

    public void update(TokenEntity token) {
        tokenMapper.update(token);
    }

    public Map<String,Object> creatToken(long userId){
        //使用随机字符串工具生成一个token字符串
        String randomString = CharUtil.getRandomString(32);
        //token创建时间
        Date date = new Date();
        //token过期最后时刻
        Date finalTime = new Date(date.getTime() + FINALTIME * 1000);

        //判断此userId下是否已经生成过token
        TokenEntity tokenEntity = queryByUserId(userId);
        //如果tokenEntity是null，说明没有生成过，需要新生成一个
        if (tokenEntity==null){
            tokenEntity = new TokenEntity();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(randomString);
            tokenEntity.setUpdateTime(date);
            tokenEntity.setFinalTime(finalTime);

            save(tokenEntity);
        }else {//创建过token，只需更新token即可
            tokenEntity.setToken(randomString);
            tokenEntity.setUpdateTime(date);
            tokenEntity.setFinalTime(finalTime);

            update(tokenEntity);
        }

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token",tokenEntity);
        hashMap.put("finalTime",FINALTIME);

        /**
         * 这里返回的是一个map,其中的token对象通过键token即可获取到
         * 因此如果要想获取到token对象就需要调用MapUtil的getString方法
         * 获取对应token;
         */
        return hashMap;
    }

}
