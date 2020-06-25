package org.example.controller;

import com.alibaba.fastjson.JSONObject;
import org.example.annotations.LoginUser;
import org.example.entity.AddressEntity;
import org.example.entity.UserEntity;
import org.example.service.AddressService;
import org.example.utils.ApiBaseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/25 上午10:06
 * @email 437547058@qq.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/address")
public class AddressController extends ApiBaseAction {
    @Autowired
    private AddressService addressService;

    /**
     * 获取用户收货地址
     */
    @PostMapping("/list")
    public Object addressList(@LoginUser UserEntity loginUser){
        Map<String,Object> addresQueryParam = new HashMap<String, Object>();
        addresQueryParam.put("user_id",loginUser.getUserId());
        List<AddressEntity> addressEntities = addressService.queryList(addresQueryParam);

        return toResponseSuccess(addressEntities);
    }

    @PostMapping("/delete")
    public Object deleteAddressById(@LoginUser UserEntity loginUser){
        try {
            JSONObject jsonRequest = this.getJsonRequest();
            Integer addressId = jsonRequest.getInteger("id");

            AddressEntity addressEntity = addressService.queryObject(addressId);

            if (!loginUser.getUserId().equals(addressEntity.getUserId())){
                return toResponsObject(403,"您无权删除","");
            }
            addressService.delete(addressId);
            return toResponseSuccess("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toResponseSuccess("");
    }
}
