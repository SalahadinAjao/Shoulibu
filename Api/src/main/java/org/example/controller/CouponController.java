package org.example.controller;

import org.example.annotations.LoginUser;
import org.example.entity.CouponEntity;
import org.example.entity.UserEntity;
import org.example.service.*;
import org.example.utils.ApiBaseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/25 上午10:58
 * @email 437547058@qq.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/coupon")
public class CouponController extends ApiBaseAction {
    @Autowired
    private ApiUserService userService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserCouponService userCouponService;

    /**
     * 获取优惠券列表
     */
    @PostMapping("/list")
    public Object couponList(@LoginUser UserEntity loginUser){
        Map couponQueryParam = new HashMap();
        couponQueryParam.put("user_id",loginUser.getUserId());

        List<CouponEntity> couponList = couponService.queryList(couponQueryParam);
        return toResponseSuccess(couponList);
    }

    /**
     * 根据商品获取可用优惠券列表
     */
    @PostMapping("/listByGood")
    public Object listByGood(@RequestParam(defaultValue = "cart") String type, @LoginUser UserEntity loginUser){

    }

}
