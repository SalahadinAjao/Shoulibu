package org.example.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.example.Util.PageTool;
import org.example.Util.Query;
import org.example.Util.ResponseTool;
import org.example.entity.SysMenuEntity;
import org.example.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/4 下午3:12
 * @email 437547058@qq.com
 * @Version 1.0
 */

@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController {
    @Autowired
    private SysMenuService menuService;


    @RequestMapping("/list")
    @RequiresPermissions("sys:menu:list")
    public ResponseTool menuList(@RequestParam Map<String,Object> map){
        //查询参数对象
        Query query = new Query(map);

        List<SysMenuEntity> entityList = menuService.queryList(query);
        int total = menuService.queryTotal(query);

        PageTool pageTool = new PageTool(entityList,total,query.getLimit(),query.getPage());

        return ResponseTool.ok().put("page",pageTool);
    }

    @RequestMapping("/select")
    @RequiresPermissions("sys:menu:select")
    public ResponseTool selectMenu(){
        List<SysMenuEntity> meunList = menuService.queryNotButtonList();

        SysMenuEntity root = new SysMenuEntity();
        root.setMenuId(0L);
        root.setName("一级菜单");
        root.setParentId(-1L);
        root.setOpen(true);

        meunList.add(root);

        return ResponseTool.ok().put("menuList",meunList);
    }


}
