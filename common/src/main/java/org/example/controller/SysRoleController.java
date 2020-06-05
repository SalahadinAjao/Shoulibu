package org.example.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.example.Util.Constant;
import org.example.Util.PageTool;
import org.example.Util.Query;
import org.example.Util.ResponseMap;
import org.example.entity.SysRoleEntity;
import org.example.service.SysRoleDeptService;
import org.example.service.SysRoleMenuService;
import org.example.service.SysRoleService;
import org.example.validator.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/4 下午12:50
 * @email 437547058@qq.com
 * @Version 1.0
 */

@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysRoleMenuService roleMenuService;
    @Autowired
    private SysRoleDeptService roleDeptService;


    /**
     *@date: 2020/6/4 下午2:47
     * 查看用户自己创建的role列表，如果调用此方法的用户
     * 是超级管理员则可以查看所有role；
     * 否则只能查看自己创建的role;
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:role:list")
    public ResponseMap roleList(@RequestParam Map<String,Object> map){
        //判断是否为超级管理员
        if (getUserId() != Constant.SUPER_ADMIN){
            //不是超级管理员，则只能查看由自己创建的角色，这里在查询的时候为普通用户添加
            //createUserId参数用以约束查询限定在此条件下
            map.put("createUserId",getUserId());
        }
        //超级管理员，可以查看所有角色，这个时候就不需要添加createUserId参数了
        Query query = new Query(map);
        List<SysRoleEntity> sysRoleEntities = roleService.queryList(query);
        int total = roleService.queryTotal(query);

        PageTool pageTool = new PageTool(sysRoleEntities, total, query.getLimit(), query.getPage());

        return ResponseMap.ok().put("page",pageTool);
    }


    @RequestMapping("/select")
    @RequiresPermissions("sys:role:select")
    public ResponseMap selectRole(){
        HashMap<String, Object> map = new HashMap<>();
        if (getUserId() != Constant.SUPER_ADMIN){
            map.put("createUserId",getUserId());
        }

        List<SysRoleEntity> entityList = roleService.queryList(map);

        return ResponseMap.ok().put("list",entityList);
    }

    @RequestMapping("/info")
    @RequiresPermissions("sys:role:info")
    public ResponseMap roleInfo(Long roleId){
        SysRoleEntity roleEntity = roleService.queryObject(roleId);

        List<Long> menuIdList = roleMenuService.queryMenuIdList(roleId);

        List<Long> deptIdList = roleDeptService.queryDeptIdList(roleId);

        roleEntity.setDeptIdList(deptIdList);
        roleEntity.setMenuIdList(menuIdList);

        return ResponseMap.ok().put("role",roleEntity);
    }

    @RequestMapping("/save")
    @RequiresPermissions("sys:role:save")
    public ResponseMap saveRole(@RequestBody SysRoleEntity roleEntity){
        //校验实体数据，在任何实体数据保存进数据库前尽量校验一下
        ValidatorUtils.validateEntity(roleEntity);
        roleEntity.setCreatorId(getUserId());

        roleService.save(roleEntity);

        return ResponseMap.ok();
    }

    @RequestMapping("/update")
    @RequiresPermissions("sys:role:update")
    public ResponseMap updateRole(@RequestBody SysRoleEntity roleEntity){
        //第一条：还是校验数据
        ValidatorUtils.validateEntity(roleEntity);

        roleEntity.setCreatorId(getUserId());
        roleService.update(roleEntity);

        return ResponseMap.ok();
    }

    @RequestMapping("/delete")
    @RequiresPermissions("sys:role:update")
    public ResponseMap deleteRole(@RequestBody Long[] roleIds){
        roleService.deleteBatch(roleIds);

        return ResponseMap.ok();
    }

}
