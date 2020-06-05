package org.example.controller;

import org.example.Util.ResponseMap;
import org.example.entity.UserEntity;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: houlintao
 * @Date:2020/5/18 上午5:22
 * @email 437547058@qq.com
 * @Version 1.0
 */

@RestController
@RequestMapping("/user")
@ResponseBody
public class UserController {
    @Autowired
    private UserService userService;


    @RequestMapping("/update")
    @ResponseBody
    public ResponseMap update(UserEntity entity){
        userService.update(entity);
        return ResponseMap.ok();
    }


    @RequestMapping("/queryAll")
    @ResponseBody
    public UserEntity[] queryAll(){
        List<UserEntity> entities = userService.queryAll();
        System.out.println("---------------");
        UserEntity[] array = (UserEntity[]) entities.toArray(new UserEntity[0]);
        return array;
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResponseMap save(@RequestBody UserEntity entity){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String estabTime = format.format(date.getTime());
        entity.setEstabTime(estabTime);
        System.out.println("前端json数据 entity = " + entity.toString());
        userService.save(entity);
        return ResponseMap.ok();
    }


}
