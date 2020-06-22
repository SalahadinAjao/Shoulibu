package org.example.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.example.annotations.LoginUser;
import org.example.dao.CouponMapper;
import org.example.entity.*;
import org.example.service.*;
import org.example.utils.ApiBaseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.soap.Addressing;
import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/21 上午11:32
 * @email 437547058@qq.com
 * @Version 1.0
 */

@RestController
@RequestMapping("/api/cart")
public class CartController extends ApiBaseAction {
    @Autowired
    private CartService cartService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ProductService productService;
    @Autowired
    private GoodsSpecificationService goodsSpecificationService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponMapper couponMapper;


    @RequestMapping("/getCart")
    public Object getCart(@LoginUser UserEntity loginUser) {
        Map<String, Object> cartQueryParam = new HashMap();
        Map<String, Object> resultObj = new HashMap();

        cartQueryParam.put("user_id", loginUser.getUserId());
        List<CartEntity> cartList = cartService.queryList(cartQueryParam);
        //购物车中商品数量
        Integer goodsQuantity = 0;
        //选中的商品数量
        Integer checkedGoodsQuality = 0;
        //购物车商品总价格
        BigDecimal goodsAmount = new BigDecimal(0.00);
        //选中的商品价格
        BigDecimal checkedGoodsAmount = new BigDecimal(0.00);

        for (CartEntity cartEntity : cartList) {
            goodsQuantity = goodsQuantity + cartEntity.getNumber();
            goodsAmount = goodsAmount.add(cartEntity.getRetail_price().multiply(new BigDecimal(cartEntity.getNumber())));

            if (cartEntity.getChecked() != null && cartEntity.getChecked() == 1) {
                checkedGoodsQuality = checkedGoodsQuality + cartEntity.getNumber();
                checkedGoodsAmount = checkedGoodsAmount.add(cartEntity.getRetail_price().multiply(new BigDecimal(cartEntity.getNumber())));
            }
        }

        //处理优惠券信息
        Map couponQueryParam = new HashMap();
        couponQueryParam.put("enabled", true);
        //优惠券发放类别
        Integer[] send_types = new Integer[]{0, 7};
        couponQueryParam.put("send_type", send_types);
        //优惠券信息列表
        List<CouponInfoEntity> couponInfoList = new ArrayList();

        List<CouponEntity> couponList = couponService.queryList(couponQueryParam);

        if (couponList != null && couponList.size() > 0) {
            //满减实体对象
            CouponInfoEntity fullCutEntity = new CouponInfoEntity();
            //满减金额
            BigDecimal fullCutDec = new BigDecimal(0);
            //激活优惠券的最低消费金额，初始化此值设置的高一些让所有优惠券默认无法使用
            BigDecimal minAmountToUseCoupon = new BigDecimal(100000);

            for (CouponEntity couponEntity : couponList) {
                //可以使用优惠券所需购买的最低商品金额-选中的商品金额=difDec——当前商品总金额与激活优惠券所需金额的差值
                BigDecimal gapDec = couponEntity.getMin_goods_amount().subtract(checkedGoodsAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
                /**
                 * 如果优惠券是按订单发放+当前商品总价不足以激活优惠券+激活优惠券的最低消费标准小于100000==>无法使用优惠券
                 */
                if (couponEntity.getSend_type() == 0 && gapDec.doubleValue() > 0.0 && minAmountToUseCoupon.compareTo(couponEntity.getMin_goods_amount()) > 0) {
                    //让满减金额fullCutDec = 优惠券的优惠金额
                    fullCutDec = couponEntity.getType_money();
                    //重置可以使用优惠券的最低消费金额，将此优惠券对象的使用门槛金额赋值给它
                    minAmountToUseCoupon = couponEntity.getMin_goods_amount();
                    /**
                     * 由于无法使用优惠券，需要将优惠券信息的描述设置为凑单
                     */
                    fullCutEntity.setType(1);
                    fullCutEntity.setMsg(couponEntity.getName() + "，还差" + gapDec + "元");
                    /**
                     * 如果优惠券是按订单发放+当前消费金额足以激活优惠券+优惠券的票面金额大于0（此时fullCutDec的值是0）
                     * 优惠券的满减金额能不能小于0？即 couponEntity.getType_money()的值小于0？
                     */
                } else if (couponEntity.getSend_type() == 0 && gapDec.doubleValue() < 0.0 && couponEntity.getType_money().compareTo(fullCutDec) > 0) {
                    //使用此优惠券对象上的满减金额给此满减金额对象赋值
                    fullCutDec = couponEntity.getType_money();
                    //当前消费金额满足可以使用优惠券的最低金额，无需凑单
                    fullCutEntity.setType(0);
                    fullCutEntity.setMsg("可使用满减券" + couponEntity.getName());
                }
                /**
                 * 如果优惠券是包邮+当前消费金额不满足优惠券的最小消费金额
                 */
                if (couponEntity.getSend_type() == 7 && gapDec.doubleValue() > 0.0) {
                    CouponInfoEntity cpEntity = new CouponInfoEntity();
                    cpEntity.setMsg("满￥" + couponEntity.getMin_amount() + "元免配送费，还差" + gapDec + "元");
                    cpEntity.setType(1);
                    couponInfoList.add(cpEntity);
                } else if (couponEntity.getSend_type() == 7) {
                    CouponInfoEntity cpEntity = new CouponInfoEntity();
                    cpEntity.setMsg("满￥" + couponEntity.getMin_amount() + "元免配送费");
                    couponInfoList.add(cpEntity);
                }
            }
            resultObj.put("couponInfoList", couponInfoList);
            resultObj.put("cartList", cartList);

            Map<String, Object> cartTotal = new HashMap();
            cartTotal.put("goodsQuantity", goodsQuantity);
            cartTotal.put("goodsAmount", goodsAmount);
            cartTotal.put("checkedGoodsQuantity", checkedGoodsQuality);
            cartTotal.put("checkedGoodsAmount", checkedGoodsAmount);

            resultObj.put("cartTotal", cartTotal);
        }
        return resultObj;
    }
    /**
     * 获取购物车信息，所有对购物车的增删改操作后都要重新返回购物车的信息
     */
    @PostMapping("/index")
    public Object index(@LoginUser UserEntity loginUser){
        return toResponseSuccess(getCart(loginUser));
    }

    /**
     * 添加商品到购物车
     */
    @PostMapping("/add")
    public Object add(@LoginUser UserEntity loginUser) throws IOException {
        JSONObject jsonParam = getJsonRequest();
        //商品id
        Integer goodsId = jsonParam.getInteger("goodsId");
        //产品id
        Integer productId = jsonParam.getInteger("productId");
        //数量
        Integer number = jsonParam.getInteger("number");

        GoodsEntity goodsEntity = goodsService.queryObject(goodsId);

        if (goodsEntity==null || goodsEntity.getIs_delete()==1 || goodsEntity.getIs_on_sale()!=1){
            return this.toResponsObject(400,"商品已下架","");
        }
        //如果商品未下架，可以购买,获取产品信息
        ProductEntity productEntity = productService.queryObject(productId);
        if (productEntity==null || productEntity.getGoods_number()<number){
            return toResponsObject(400,"库存不足","");
        }
        //如果商品可以购买且库存充足

        Map cartParam = new HashMap();
        cartParam.put("goods_id",goodsId);
        cartParam.put("product_id",productId);
        cartParam.put("user_id",loginUser.getUserId());

        List<CartEntity> cartList = cartService.queryList(cartParam);
        CartEntity cartInfo = null;
        if (cartList!=null && cartList.size()>0){
            cartInfo=cartList.get(0);
        }else {
            cartInfo=null;
        }
        //如果购物车不存在就新建一个
        if (cartInfo==null){
            String[] goodsSpecificationValue = null;
            /**
             * productEntity是前端传递过来的，通过它可以获取此产品的不同规格的id
             */
            if (productEntity.getGoods_specification_ids()!=null && productEntity.getGoods_specification_ids().length()>0){
                Map specificationParam = new HashMap();
                //把productEntity的规格id以"_"分开
                String[] idsArray = getSpecificationIdsArray(productEntity.getGoods_specification_ids());
                specificationParam.put("ids",idsArray);
                specificationParam.put("goods_id",goodsId);
                List<GoodsSpecificationEntity> specificationList = goodsSpecificationService.queryList(specificationParam);
                goodsSpecificationValue = new String[specificationList.size()];

                for (int i=0;i<specificationList.size();i++){
                    goodsSpecificationValue[i] = specificationList.get(i).getValue();
                }
            }
            cartInfo = new CartEntity();

            cartInfo.setGoods_id(goodsId);
            cartInfo.setProduct_id(productId);
            cartInfo.setGoods_sn(productEntity.getGoods_sn());
            cartInfo.setGoods_name(goodsEntity.getName());
            cartInfo.setList_pic_url(goodsEntity.getList_pic_url());
            cartInfo.setNumber(number);
            cartInfo.setSession_id("1");
            cartInfo.setUser_id(loginUser.getUserId());
            cartInfo.setRetail_price(productEntity.getRetail_price());
            cartInfo.setMarket_price(productEntity.getMarket_price());

            if (goodsSpecificationValue!=null){
                cartInfo.setGoods_specifition_name_value(com.qiniu.util.StringUtils.join(goodsSpecificationValue,";"));
            }
            cartInfo.setGoods_specifition_ids(productEntity.getGoods_specification_ids());
            cartInfo.setChecked(1);

            cartService.save(cartInfo);
        }else {
            //如果已经存在购物车中，则数量增加
            //从数据库查询到的product数量小于(前端购买的数量+购物车中的数量)
            if (productEntity.getGoods_number()<(number+cartInfo.getNumber())){
                return this.toResponsObject(400, "库存不足", "");
            }
            cartInfo.setNumber(number+cartInfo.getNumber());
            cartService.update(cartInfo);
        }
        return toResponseSuccess(getCart(loginUser));
    }

    public String[] getSpecificationIdsArray(String ids){
        String[] idsArray = null;
        if (StringUtils.isNotEmpty(ids)){
            String[] tempArray = ids.split("_");
            if (tempArray!=null && tempArray.length>0){
                idsArray = tempArray;
            }
        }
        return idsArray;
    }
}
