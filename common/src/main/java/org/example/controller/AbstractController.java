package org.example.controller;

import org.example.Util.ShiroUtils;
import org.example.entity.SysUserEntity;

/**
 * @Author: houlintao
 * @Date:2020/6/1 下午2:46
 * @email 437547058@qq.com
 * @Version 1.0
 */
public abstract class AbstractController {

    public static final Long serialVersionUID=1L;

    public SysUserEntity getUser(){
        return ShiroUtils.getUserEntity();
    }

    public Long getUserId(){
        return getUser().getUserId();
    }
}
