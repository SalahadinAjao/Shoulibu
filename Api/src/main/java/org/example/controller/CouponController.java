package org.example.controller;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.alibaba.fastjson.JSONObject;
import org.example.Util.CharUtil;
import org.example.Util.StringUtils;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

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
            //如果是type的值为cart，说明是购物车支付=>从数据库查询订单信息，购物车信息
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
     /**
      * 使用优惠码兑换优惠券
      * 优惠码在用户点击“获取优惠码”后由系统创建后通过SMS(Short Message Service)服务以短信的方式发送给用户
      * 或者直接由程序返回给前端；
      * 在后端创建优惠码的时候需要新建一个优惠券对象将此优惠码与优惠券绑定并存储进数据库，同时需要创建一个用户优惠券对象
      * 将新建的优惠券对象id传进去并设置其未兑换；
      */
     @PostMapping("/exchangeCoupon")
     public Object exchangeCoupon(@LoginUser UserEntity looginUser){
         try {
             JSONObject jsonRequest = getJsonRequest();
             String coupon_number = jsonRequest.getString("coupon_number");

             if (StringUtils.isNullOrEmpty(coupon_number)){
                 return toResponsFail("当前优惠码不存在");
             }
             Map userCouponParam = new HashMap();
             userCouponParam.put("coupon_number",coupon_number);
             List<UserCouponEntity> userCouponList = userCouponService.queryList(userCouponParam);

             UserCouponEntity userCouponEntity = null;

             if (userCouponList == null || userCouponList.size()==0){
                 return toResponsFail("当前优惠码无效");
             }
             //一个优惠码只能对应一个UserCouponEntity
             userCouponEntity = userCouponList.get(0);
             /**
              * 当一个优惠码刚被创建时就会在系统创建对应的用户优惠券对象并将此对象的userId为0L，一旦此优惠码被兑换成优惠券系统就会
              * 将用户优惠券对象的userId设置为当前兑换用户的userId；
              */
             if (userCouponEntity.getUser_id()!=null && !userCouponEntity.getUser_id().equals(0L)){
                 return toResponsFail("当前优惠码已被兑换");
             }

             CouponEntity couponEntity = couponService.queryObject(userCouponEntity.getCoupon_id());
             if (couponEntity==null || couponEntity.getUse_end_date()==null || couponEntity.getUse_end_date().before(new Date())){
                 return toResponsFail("优惠码已过期");
             }
             userCouponEntity.setUser_id(looginUser.getUserId());
             userCouponEntity.setAdd_time(new Date());

             userCouponService.update(userCouponEntity);

             return toResponseSuccess(userCouponEntity);
         } catch (IOException e) {
             e.printStackTrace();
         }
         return null;
     }
     /**
      * 填写手机号和验证码领券，新用户专属，这一步的使用场景是用户需要先输入手机号获取到验证码，然后将手机号和验证码
      * 一并作为参数提交到后台。只有用户第一次填写手机号才可以领取，以后更改手机号是不可以领取了。所以重点是如何判断用户
      * 是新用户还是老用户：新用户的用户实体中的手机号属性是null的而老用户则相反！
      * 新用户领取优惠券
      */
     @PostMapping("/newUserCoupon")
     public Object newUserCoupon(@LoginUser UserEntity loginUser) throws IOException {
         JSONObject jsonRequest = getJsonRequest();
         //从请求对象中获取手机号
         String phone = jsonRequest.getString("phone");
         //从请求对象获取验证码
         Integer smsCode = jsonRequest.getInteger("smsCode");
         /**
          * 通过userId查找对应的短信日志记录，由于验证码实效是30min，超过30min验证码失效，因此不需要担心通过userId查询到
          * 多条smsLog实体，因为在验证码失效前查到的就只有一条记录，失效后就查不到。
          */
         SmsLogEntity smsLogEntity = userService.querySmsCodeByUserId(loginUser.getUserId());

         if (smsLogEntity!=null && smsLogEntity.getSms_code()!=smsCode){
             return toResponsFail("验证码错误");
         }
         //验证码无误则更新手机号
         if (!StringUtils.isNullOrEmpty(phone)){
             /**
              * 拿用户前端传递过来的phone的值与当前登录系统的用户的getMobile()方法返回的手机号对比：
              * 如果相等=>是老用户，老用户不做处理；
              * 如果是新用户则getMobile方法返回的是null，与phone并不相等=>新用户；
              */
             if (phone.equals(loginUser.getMobile())){
                 loginUser.setMobile(phone);
                 userService.update(loginUser);
             }
         }
         //判断是否为新用户，如果是老用户=> loginUser.getMobile()返回值不为null
          if (!StringUtils.isNullOrEmpty(loginUser.getMobile())){
              return toResponsFail("非新注册用户无法领取");
          }
          //如果当前用户是新用户，则判断其是否已经领取过了
         Map couponParam = new HashMap();
          couponParam.put("user_id",loginUser.getUserId());
          //send_type=4 =>新用户优惠券
          couponParam.put("send_type",4);

         List<UserCouponEntity> userCouponEntityList = couponService.queryUserCoupons(couponParam);

         if (userCouponEntityList != null && userCouponEntityList.size()>0){
             return toResponsFail("已领券，请不要重复领取");
         }
         //新用户领券
         Map couponQuery = new HashMap();
         couponQuery.put("send_type",4);
         //基于send_type分类查询用户可用的抵扣值最大的优惠券，即在这里会返回一个新用户可用的面值最大的优惠券
         CouponEntity couponEntity = couponService.queryUserMaxCoupon(couponQuery);

         if (couponEntity!=null){
             UserCouponEntity userCouponEntity = new UserCouponEntity();
             userCouponEntity.setAdd_time(new Date());
             userCouponEntity.setCoupon_id(couponEntity.getId());
             userCouponEntity.setCoupon_number(CharUtil.getRandomString(12));
             userCouponEntity.setUser_id(loginUser.getUserId());

             userCouponService.save(userCouponEntity);

             return toResponseSuccess(userCouponEntity);
         }else {
             return toResponsFail("领取失败");
         }
     }
    /**
     * 转发领红包
     * @param loginUser 当前用户
     * @param sourceKey 转发的来源的key
     * @param referrer 转发分享人的id
     */
     @PostMapping("transActivit")
     public Object transAActivit(@LoginUser UserEntity loginUser,String sourceKey,Long referrer) throws IOException {
         JSONObject jsonRequest = getJsonRequest();

         Map couponParam = new HashMap();

         couponParam.put("user_id",loginUser.getUserId());
         //send_type=2 是商品转发送的券
         couponParam.put("send_type",2);
         couponParam.put("source_key",sourceKey);

         List<CouponEntity> coupons = couponService.queryUserCoupons(couponParam);

         if (coupons!=null && coupons.size()>0){
             return toResponsObject(2,"已经领取过了",coupons);
         }
         //领取
         Map couponQueryParam = new HashMap();
         //转发赠券
         couponQueryParam.put("send_type",2);
         //获取满足条件的用户可用最大面值优惠券
         /**
          * 背景：在设计优惠券的时候需要考虑同一发券类型下的不同属性优惠券的问题。例如当我发一个用户转发送的优惠券时，send_type=2，此时
          * 不可能我只最终发布一种面值为5元的优惠券，我还可以以后随着业务发展设计一种面值为1的优惠券，以此类推。因此在数据库中的优惠券表中
          * 当send_type字段值为2的时候可以有多个面值，使用日期等不同的优惠券。
          * 而在新用户注册时候发给用户的优惠券一般设置同类发布类型中面值最大的。
          */
         CouponEntity couponEntity = couponService.queryUserMaxCoupon(couponQueryParam);

         if (couponEntity!=null){
             //用户优惠券
             UserCouponEntity userCouponEntity = new UserCouponEntity();
             //设置领券时间
             userCouponEntity.setAdd_time(new Date());
             userCouponEntity.setCoupon_id(couponEntity.getId());
             //设置优惠券号码
             userCouponEntity.setCoupon_number(CharUtil.getRandomString(12));
             userCouponEntity.setUser_id(loginUser.getUserId());
             userCouponEntity.setSource_key(sourceKey);
             userCouponEntity.setReferrer(referrer);
             //将此用户优惠券对象存入数据库
             userCouponService.save(userCouponEntity);

             List<UserCouponEntity> userCOuponList = new ArrayList<>();
             userCOuponList.add(userCouponEntity);

             couponParam.put("user_id",loginUser.getUserId());
             couponParam.put("send_type",2);
             couponParam.put("sourec_key",sourceKey);

            coupons = couponService.queryUserCoupons(couponParam);

            return toResponseSuccess(coupons);
         }else {
             return toResponsFail("领取失败");
         }
     }
}
