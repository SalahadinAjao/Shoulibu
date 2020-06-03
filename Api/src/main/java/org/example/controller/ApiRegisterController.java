package org.example.controller;

import org.example.Util.ResponseTool;
import org.example.service.ApiUserService;
import org.example.validator.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: houlintao
 * @Date:2020/5/31 下午4:55
 * @email 437547058@qq.com
 * @Version 1.0
 * 用户注册控制器
 */

@RestController
@RequestMapping("api/register")
public class ApiRegisterController {

    @Autowired
    private ApiUserService userService;
    /**
     *@date: 2020/5/31 下午4:59
     *@param:
     *@return:
     *@Description:用户注册方法
     */
    @RequestMapping("register")
    public ResponseTool register(String mobile, String password){
        Assert.isBlank(mobile,"手机号不能为空");
        Assert.isBlank(password,"密码不能为空");

        userService.save(mobile,password);

        return ResponseTool.ok();
    }
}
