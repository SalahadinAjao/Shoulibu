package org.example.controller;

import org.example.Util.cache.J2CacheTool;
import org.example.annotations.LoginUser;
import org.example.entity.*;
import org.example.service.*;
import org.example.utils.ApiBaseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
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
     * 这里使用@RequestParam注解在type参数上，设置其默认值是cart，也就是说不管前端有没有
     * 向此方法传入type的值，此变量默认值是cart==>即使前端没有传值进来此参数的值也默认为cart；
     *
     */
    @PostMapping("/listByGood")
    public Object listByGood(@RequestParam(defaultValue = "cart") String type, @LoginUser UserEntity loginUser){
        //所选中的商品总价
        BigDecimal goodsTotalPrice = new BigDecimal(0.00);

        if (type.equals("cart")){
            Map cartParam = new HashMap();
            cartParam.put("user_id",loginUser.getUserId());
            //查询此用户的所有购物车
            List<CartEntity> cartList = cartService.queryList(cartParam);

            for (CartEntity cart : cartList){
                /**
                 * 返回的list中包含了你选中的cart和未选中的cart，只有被选中的cart才参与计算
                 */
                if (cart.getChecked()!=null && cart.getChecked()==1){
                    /**
                     * 这里需要注意一点就是变量goodsTotalPrice是一个BigDecimal类型的，它相加的对象也需要是个BigDecimal类型
                     * 因此add()方法内部参数需要是个BigDecimal。而 @code cart.getRetail_price()的返回值是个BigDecimal，因此
                     * 需要将number也封装成一个BigDecimal。
                     */
                    goodsTotalPrice=goodsTotalPrice.add(cart.getRetail_price().multiply(new BigDecimal(cart.getNumber())));
                }
            }
            /**
             * 如果type的值不是cart，说明是直接购买的，此时商品是被缓存在J2Cache中的，只有用户的购物车对象才会被存入数据库。
             * 用户在购物的时候有两种不同的场景：直接购买和加入购物车
             * 直接购买： 用户直接购买的场景是看中某款产品就直接购买，支付；若选中产品并点击“立即购买”后没有由于种种原因没有立即付款或者
             * 点击“立即付款”后没有立即付款，在这种场景下系统后台需要将此订单进行缓存一个特定时间（如京东缓存12h），在缓存到期时间内
             * 可以接着支付，超过缓存时间则缓存失效；在用户点击“立即付款”按钮的时候就会将其所选择的产品加入缓存。
             *
             * 加入购物车： 购物车存在的意义之一就是要提供给用户一个将所选的中意产品或临时(例如加入购物车后几个产品立即付款)或长期存放(
             * 例如今天加入购物车下周才付款)的容器，它提供永久保存用户所选商品的功能，因此它需要被保存入数据库。这样用户哪怕是在加入购物车
             * 后过了一年再次通过他的id也可以找到原来的购物车。
             * */
        }else {
            BuyGoodsEntity buyGoodsEntity = (BuyGoodsEntity) J2CacheTool.get(J2CacheTool.SHOP_CACHE_NAME,"goods"+loginUser.getUserId()+"");
            //获取对应的产品实体对象
            ProductEntity productEntity = productService.queryObject(buyGoodsEntity.getProductId());

            goodsTotalPrice = productEntity.getRetail_price().multiply(new BigDecimal(buyGoodsEntity.getNumber()));
        }
        //处理优惠券相关
        Map couponParam = new HashMap();
        couponParam.put("user_id",loginUser.getUserId());
        //优惠券状态 1 可用 2 已用 3 过期
        couponParam.put("coupon_status",1);

        List<CouponEntity> userCoupons = couponService.queryUserCoupons(couponParam);
        //可用优惠券集合
        List<CouponEntity> enableCoupons = new ArrayList<>();
        //不可用优惠券集合
        List<CouponEntity> unenableCoupons = new ArrayList<>();

        for (CouponEntity coupon : userCoupons){
            /**
             * 用用户消费产品总金额与每个优惠券的最低使用金额比较，若用户消费金额大于
             * 优惠券设置的最低消费标准就可以使用此优惠券；
             * 反之就不可以。
             */
            if (coupon.getMin_amount().compareTo(goodsTotalPrice)<0){
                //优惠券可用
                coupon.setEnabled(1);
                enableCoupons.add(coupon);
            }else if (coupon.getMin_amount().compareTo(goodsTotalPrice)>=0){
                //优惠券不可用
                coupon.setEnabled(0);
                unenableCoupons.add(coupon);
            }
        }
        //这里将所有不可用的优惠券加入所有可用的优惠券中一并返回前端，前端根据可用状态处理UI
        enableCoupons.addAll(unenableCoupons);
        return toResponseSuccess(enableCoupons);
    }


}
