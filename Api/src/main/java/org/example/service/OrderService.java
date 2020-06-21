package org.example.service;

import com.alibaba.fastjson.JSONObject;
import org.example.entity.*;
import org.example.Util.CommonTool;
import org.example.Util.cache.J2CacheTool;
import org.example.dao.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: houlintao
 * @Date:2020/6/5 下午5:48
 * @email 437547058@qq.com
 * @Version 1.0
 */

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderDao;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private OrderGoodsMapper orderGoodsMapper;
    @Autowired
    private ProductService productService;

    public OrderEntity queryOrderObjectByOrderSn(String orderSn){
        return orderMapper.queryObjectByOrderSn(orderSn);
    }

    public OrderEntity queryOrderObjectById(Integer id){
        return orderMapper.queryObject(id);
    }

    public List<OrderEntity> queryOrderList(Map<String,Object> map){
        return orderMapper.queryList(map);
    }

    public int queryTotalOrder(Map<String,Object> map){
        return orderMapper.queryTotal(map);
    }

    public void saveOrder(OrderEntity entity){
        orderMapper.save(entity);
    }

    public int updateOrder(OrderEntity entity){
        return orderMapper.update(entity);
    }

    public void deleteOrder(Integer id){
        orderMapper.delete(id);
    }

    public void batchDeleteOrder(Integer[] ids){
        orderMapper.deleteBatch(ids);
    }

    /**
     * 当把@Transactional 注解放在类级别时，表示所有该类的公共方法都配置相同的事务属性信息;
     * 在应用系统调用声明@Transactional 的目标方法时，Spring Framework 默认使用 AOP 代理，
     * 在代码运行时生成一个代理对象，根据@Transactional 的属性配置信息，这个代理对象决定该声
     * 明@Transactional 的目标方法是否由拦截器 TransactionInterceptor 来使用拦截，在
     * TransactionInterceptor 拦截时，会在在目标方法开始执行之前创建并加入事务，并执行目标方法的逻辑,
     * 最后根据执行情况是否出现异常，利用抽象事务管理器;
     * https://www.cnblogs.com/xd502djj/p/10940627.html
     */
    @Transactional
    public Map<String,Object> submit(JSONObject jsonParam, UserEntity userEntity){
        //新建一个map对象用于存放结果数据
        HashMap<String, Object> resultObj = new HashMap<>();
        //从jsonParam中获取与本次交易绑定的优惠券id
        Integer couponid = jsonParam.getInteger("couponId");
        /**
         * 这个type是由前端决定其具体内容，当前端选择立即购买时此type=buy;
         * 当前端选择加入购物车再购买时此type = cart
         */
        String type = jsonParam.getString("type");
        //额外信息，如用户留言
        String postscript = jsonParam.getString("postscript");
        //获取用户地址
        AddressEntity addressEntity = addressMapper.queryObject(jsonParam.getInteger("addressId"));
        //运费
        Integer freightPrice = 0;
        //选中的商品的购物车列表
        List<CartEntity> checkedGoodsCartList = new ArrayList<>();
        //商品总价
        BigDecimal goodsTotalPrice= null;
        /**
         * 如果是从购物车提交的就需要为其分配购物车了
         */
        if (type.equals("cart")){
            HashMap<String, Object> cartMap = new HashMap<>();

            cartMap.put("user_id",userEntity.getUserId());
            cartMap.put("session_id",1);
            //checked=1表示选中
            cartMap.put("checked",1);

            /**
             * 从数据库的cart表中取出对应的购物车列表，实际上在用户在前端将一个选中的商品加入购物车的时候系统
             * 就会为此商品分配对应的购物车，也就是一款产品对应着一个购物车，并不是n个产品都放在一个购物车中；
             * 用户添加n个产品就会进n个购物车，在用户完成清空购物车或者删除购物车时购物车会被删除；
             */
            checkedGoodsCartList = cartMapper.queryList(cartMap);

            if (checkedGoodsCartList == null){
                resultObj.put("errno",400);
                resultObj.put("errmsg","请选择商品");

                return resultObj;
            }
            //如果购物车列表不为空
            goodsTotalPrice = new BigDecimal(0.00);
            /**
             * 我们平时通过电商购物（如京东，天猫，拼多多）的购物车其实相当于一个购物车列表
             * 一个购物车包含了一款产品的数量，价格等属性；
             * 我们加进去的商品，如1台笔记本，价格49999是一个购物车a，3张画是一个购物车b
             * a和b是独立的；
             * 我们通过APP的“购物车”页面看到的购物车内容是a和b组成的列表
             */
            for (CartEntity cartEntity:checkedGoodsCartList){
                goodsTotalPrice = goodsTotalPrice.add(cartEntity.getRetail_price().multiply(new BigDecimal(cartEntity.getNumber())));
            }
        }else {//直接购买商品
            //buyGoodsEntity表示我们购买的商品
            BuyGoodsEntity buyGoodsEntity = (BuyGoodsEntity) J2CacheTool.get(J2CacheTool.SHOP_CACHE_NAME, "goods" + userEntity.getUserId());
            //通过我们所购买的产品buyGoodsEntity获取到产品id,进而获取到对应的productEntity
            ProductEntity productEntity = productService.queryObject(buyGoodsEntity.getProductId());
            //计算商品总价商品总价=商品零售价*数量，这里用的是BigDEcimal
            goodsTotalPrice = productEntity.getRetail_price().multiply(new BigDecimal(buyGoodsEntity.getName()));
            //分配购物车
            CartEntity cartEntity = new CartEntity();
            /**
             * 由于在这个上下文语境下 cartEntity包含很多productEntity的属性，因此
             * 这里使用深拷贝把productEntity的属性直接拷贝进cartEntity，再在cartEntity中
             * 按需修改
             */
            BeanUtils.copyProperties(productEntity,cartEntity);
            //根据用户实际购买数量修改购物车中商品数量和对应产品id
            cartEntity.setNumber(buyGoodsEntity.getNumber());
            cartEntity.setProduct_id(buyGoodsEntity.getProductId());

            checkedGoodsCartList.add(cartEntity);
        }

        //获取与此订单相关的优惠券信息
        BigDecimal couponPrice = new BigDecimal(0.00);
        CouponEntity couponEntity = null;

        if (couponid != null && couponid != 0){
            couponEntity = couponMapper.getUserCoupon(couponid);

            if (couponEntity != null && couponEntity.getCoupon_status()==1){
                couponPrice = couponEntity.getType_money();
            }

        }
        /**
         * 订单价格等于商品总价格+运费-折扣优惠券等
         */
        BigDecimal orderTotalPrice = goodsTotalPrice.add(new BigDecimal(freightPrice));
        BigDecimal finalOrderPrice = orderTotalPrice.subtract(couponPrice);

        Long currentTime = System.currentTimeMillis()/1000;

        OrderEntity orderInfo = new OrderEntity();
        orderInfo.setOrder_sn(CommonTool.createOrderSn());
        orderInfo.setUser_id(userEntity.getUserId());
        orderInfo.setConsignee(addressEntity.getUserName());
        orderInfo.setMobile(userEntity.getMobile());
        orderInfo.setCountry(addressEntity.getNationalCode());
        orderInfo.setProvince(addressEntity.getProvinceName());
        orderInfo.setCity(addressEntity.getCityName());
        orderInfo.setDistrict(addressEntity.getCountyName());
        orderInfo.setAddress(addressEntity.getDetailInfo());
        //设置运费
        orderInfo.setFreight_price(freightPrice);
        //设置留言
        orderInfo.setPostscript(postscript);
        //使用优惠券
        orderInfo.setCoupon_id(couponid);
        orderInfo.setCoupon_price(couponPrice);
        orderInfo.setAdd_time(new Date());
        orderInfo.setGoods_price(goodsTotalPrice);
        orderInfo.setOrder_price(orderTotalPrice);
        orderInfo.setActual_price(finalOrderPrice);
        //待付款
        orderInfo.setOrder_status(0);
        orderInfo.setShopping_status(0);
        orderInfo.setPay_status(0);
        orderInfo.setShipping_id(0);
        orderInfo.setShipping_fee(new BigDecimal(0));
        //设置积分
        orderInfo.setIntegral(0);
        orderInfo.setIntegral_money(new BigDecimal(0));
        //设置订单类别，若是在购物车结算就是1
        if (type.equals("cart")){
            orderInfo.setOrder_type("1");
        }else {
            orderInfo.setOrder_type("4");
        }
        //开启事务，插入订单信息和订单商品
        orderMapper.save(orderInfo);
        if (orderInfo.getId() == null){
            resultObj.put("errno",1);
            resultObj.put("errmsg","订单提交失败");
            return resultObj;
        }
        ArrayList<OrderGoodsEntity> orderGoodsEntities = new ArrayList<>();

        for (CartEntity goodsItem:checkedGoodsCartList){

            OrderGoodsEntity orderGoodsEntity = new OrderGoodsEntity();

            orderGoodsEntity.setOrder_id(orderInfo.getId());
            orderGoodsEntity.setGoods_id(goodsItem.getGoods_id());
            orderGoodsEntity.setGoods_sn(goodsItem.getGoods_sn());
            orderGoodsEntity.setProduct_id(goodsItem.getProduct_id());
            orderGoodsEntity.setGoods_name(goodsItem.getGoods_name());
            orderGoodsEntity.setList_pic_url(goodsItem.getList_pic_url());
            orderGoodsEntity.setMarket_price(goodsItem.getMarket_price());
            orderGoodsEntity.setRetail_price(goodsItem.getRetail_price());
            orderGoodsEntity.setNumber(goodsItem.getNumber());
            orderGoodsEntity.setGoods_specifition_name_value(goodsItem.getGoods_specifition_name_value());
            orderGoodsEntity.setGoods_specifition_ids(goodsItem.getGoods_specifition_ids());

            orderGoodsEntities.add(orderGoodsEntity);

            orderGoodsMapper.save(orderGoodsEntity);
        }
        //清空已购买的商品，在用户清空购物车的时候
            cartMapper.deleteByCart(userEntity.getUserId(),1,1);
            resultObj.put("errno", 0);
            resultObj.put("errmsg", "订单提交成功");

        HashMap<String, OrderEntity> orderMap = new HashMap<>();
        orderMap.put("orderInfo",orderInfo);
        resultObj.put("data",orderMap);

        //将优惠券标志为已用
        if (couponEntity != null && couponEntity.getCoupon_status()==1){
            couponEntity.setCoupon_status(2);
            couponMapper.updateUserCoupon(couponEntity);
        }
        return resultObj;
    }
}
