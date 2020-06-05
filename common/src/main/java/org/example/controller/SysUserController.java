package org.example.controller;

import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.example.Util.*;
import org.example.entity.SysUserEntity;
import org.example.service.SysUserRoleService;
import org.example.service.SysUserService;
import org.example.validator.Assert;
import org.example.validator.ValidatorUtils;
import org.example.validator.group.AddGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/1 下午2:38
 * @email 437547058@qq.com
 * @Version 1.0
 * 系统用户控制器类，负责允许管理员管理这个系统中的用户，对用户进行增删查改操作
 */

@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysUserRoleService userRoleService;


    @RequestMapping("/list")
    @RequiresPermissions("sys:user:list")
    public ResponseMap List(@RequestParam Map<String,Object> map){
        if (getUserId() != Constant.SUPER_ADMIN){
            map.put("createUserId",getUserId());
        }
        Query query = new Query(map);
        List<SysUserEntity> entityList = userService.queryList(query);
        int total = userService.queryTotal(map);

        PageTool pageTool = new PageTool(entityList, total, query.getLimit(), query.getPage());

       return ResponseMap.ok().put("page",pageTool);
    }

    /**
     * 获取当前登录的用户信息
     */
    @RequestMapping("/info")
    public ResponseMap info() {
        return ResponseMap.ok().put("user", getUser());
    }
    /**
     *@date: 2020/6/1 下午6:19
     *@param oldPass 旧密码
     *@param newPass 新密码
     *@return:
     *@Description:修改密码
     */
    public ResponseMap updatePassword(String oldPass, String newPass){
       if (ResourceTool.getConfigPropertyByName("sys.demo").equals("1")){
           throw new RRException("演示环境，无法修改密码");
       }
        Assert.isBlank(newPass,"新密码不能为空");
       //使用sha256给密码加密
        oldPass = new Sha256Hash(oldPass).toHex();
        newPass = new Sha256Hash(newPass).toHex();

        int password = userService.updatePassword(getUserId(), oldPass, newPass);
        if (password==0){
            return ResponseMap.error("原密码错误");
        }
        SecurityUtils.getSubject().logout();
        return ResponseMap.ok();
    }

    /**
     * 根据此用户id获取对应的用户实体信息，包括此用户相关的角色信息
     */
    @RequestMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    public ResponseMap userInfo(@PathVariable("userId") Long userId){
        SysUserEntity userEntity = userService.queryObject(userId);
        //获取与此用户相关的角色id列表
        List<Long> roleIdList = userRoleService.queryRoleIdList(userId);

        userEntity.setRoleIdList(roleIdList);
        return ResponseMap.ok().put("user",userEntity);
    }


    @RequestMapping("/save")
    @RequiresPermissions("sys:user:save")
    public ResponseMap saveUser(@RequestBody SysUserEntity userEntity){
        ValidatorUtils.validateEntity(userEntity, AddGroup.class);
        userEntity.setCreateUserId(getUserId());
        userService.save(userEntity);

        return ResponseMap.ok();
    }

    /**
     *@date: 2020/6/3 下午4:33
     *@param:
     *@return:
     *@Description:更新用户信息
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:user:update")
    public ResponseMap updateUser(@RequestBody SysUserEntity userEntity){
        //同save方法一样，更新用户信息的第一步是校验数据
        Map<String, StringBuilder> validateEntity = ValidatorUtils.validateEntity(userEntity);
        userEntity.setCreateUserId(getUserId());
        userService.update(userEntity);

        return ResponseMap.ok();
    }

    @RequestMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public ResponseMap deleteUser(@RequestBody Long[] userIds){
        //判断系统用户级别，系统管理员无法删除
        if (ArrayUtils.contains(userIds,1L)){
            return ResponseMap.error("系统管理员，无法删除");
        }
        userService.deleteBatch(userIds);

        return ResponseMap.ok();
    }

}
