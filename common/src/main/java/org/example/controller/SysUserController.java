package org.example.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.example.Util.*;
import org.example.entity.SysUserEntity;
import org.example.service.SysUserRoleService;
import org.example.service.SysUserService;
import org.example.validator.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/1 下午2:38
 * @email 437547058@qq.com
 * @Version 1.0
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
    public R List(@RequestParam Map<String,Object> map){
        if (getUserId() != Constant.SUPER_ADMIN){
            map.put("createUserId",getUserId());
        }
        Query query = new Query(map);
        List<SysUserEntity> entityList = userService.queryList(query);
        int total = userService.queryTotal(map);

        PageTool pageTool = new PageTool(entityList, total, query.getLimit(), query.getPage());

       return R.ok().put("page",pageTool);
    }

    /**
     * 获取当前登录的用户信息
     */
    @RequestMapping("/info")
    public R info() {
        return R.ok().put("user", getUser());
    }
    /**
     *@date: 2020/6/1 下午6:19
     *@param oldPass 旧密码
     *@param newPass 新密码
     *@return:
     *@Description:修改密码
     */
    public R updatePassword(String oldPass,String newPass){
       if (ResourceTool.getConfigPropertyByName("sys.demo").equals("1")){
           throw new RRException("演示环境，无法修改密码");
       }
        Assert.isBlank(newPass,"新密码不能为空");
       //使用sha256给密码加密
        oldPass = new Sha256Hash(oldPass).toHex();
        newPass = new Sha256Hash(newPass).toHex();

        int password = userService.updatePassword(getUserId(), oldPass, newPass);
        if (password==0){
            return R.error("原密码错误");
        }
        SecurityUtils.getSubject().logout();
        return R.ok();
    }

}
