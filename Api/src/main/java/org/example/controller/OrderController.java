package org.example.controller;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Hash;
import org.example.Util.PageTool;
import org.example.Util.Query;
import org.example.annotations.LoginUser;
import org.example.entity.OrderEntity;
import org.example.entity.OrderGoodsEntity;
import org.example.entity.UserEntity;
import org.example.service.KdNiaoService;
import org.example.service.OrderGoodsService;
import org.example.service.OrderService;
import org.example.utils.ApiBaseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/7 上午9:26
 * @email 437547058@qq.com
 * @Version 1.0
 */

@RestController
@RequestMapping("/api/order")
public class OrderController extends ApiBaseAction {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private KdNiaoService kdNiaoService;


    /**
     * 获取订单列表
     * @param userEntity
     * @param page
     */
    public Object getList(@LoginUser UserEntity userEntity,
                          @RequestParam(value = "page",defaultValue = "1") Integer page,
                          @RequestParam(value = "size",defaultValue = "10") Integer size){
        //先声明一个map用以存储参数
        HashMap<String, Object> params = new HashMap<>();
        params.put("user_id",userEntity.getUserId());
        params.put("page",page);
        params.put("limit",size);
        params.put("sidx","id");
        params.put("order","asc");

        Query query = new Query(params);

        List<OrderEntity> orderEntities = orderService.queryOrderList(query);
        int total = orderService.queryTotalOrder(query);

        //使用page工具类约束向前端返回的数据分页
        PageTool pageTool = new PageTool(orderEntities, total, query.getLimit(), query.getPage());

        for (OrderEntity order:orderEntities){
            HashMap orderGoodsParam = new HashMap();
            /**
             * 一个Order中对应多个Good对象组成的订单，通过order_id获取对应的Order对象;
             * 再通过此Order对象获取此对象中保存的所有Good对象以及其数量等数据；
             */
            orderGoodsParam.put("order_id",order.getId());
            //通过orderGoodsService查询本订单中的商品信息
            List<OrderGoodsEntity> goodsList = orderGoodsService.queryList(orderGoodsParam);
            Integer goodsCount = null;
            for (OrderGoodsEntity goodsEntity:goodsList){
                goodsCount+=goodsEntity.getNumber();

                order.setGoodsCount(goodsCount);
            }
        }
        return toResponseSuccess(pageTool);
    }
    /**
     * 获取订单列表中的某一项的详情，例如前端点击订单列表中的某一项就会
     * 调用此方法；
     */
    @RequestMapping("/detail")
    public Object getOrderDetail(Integer oderId) throws Exception {
        //声明一个hashMap对象作为返回对象
        Map resultObj = new HashMap<>();
        OrderEntity orderEntity = orderService.queryOrderObjectById(oderId);
        if (orderEntity==null){
            return toResponsObject(400,"订单不存在","");
        }
        //如果订单存在就声明一个map对象存储orderGood参数
        Map orderGoodParams = new HashMap();
        orderGoodParams.put("order_id",oderId);
        //获取对应订单中的商品
        List<OrderGoodsEntity> orderGoodsList = orderGoodsService.queryList(orderGoodParams);
        if (orderEntity.getOrder_status() == 0){//状态0表示订单创建成功等待付款

        }

        Map handleOption = orderEntity.getHandleOption();
        resultObj.put("orderInfo",orderEntity);
        resultObj.put("orderGoods",orderGoodsList);
        resultObj.put("handleOption",handleOption);
        /**
         * 本订单的物流信息
         */
        if (!StringUtils.isEmpty(orderEntity.getShipping_code()) && !StringUtils.isEmpty(orderEntity.getShipping_no())){
            //通过第三方快递鸟接口获取快递物流信息
            List shippingInfo = kdNiaoService.TraceOrderWithJson(orderEntity.getShipping_code(), orderEntity.getShipping_no());
            resultObj.put("shippingInfo",shippingInfo);
        }
        return toResponseSuccess(resultObj);
    }
    /**
     * 修改订单
     */
    @RequestMapping("/update")
    public Object updateOrder(Integer id){
        OrderEntity orderEntity = orderService.queryOrderObjectById(id);
        if (orderEntity==null){
            return toResponsFail("订单不存在");
        }else if (orderEntity.getOrder_status()!=0){
            return toResponsFail("订单状态不正确orderStatus"+orderEntity.getOrder_status()+"payStatus"+orderEntity.getPay_status());
        }
        orderEntity.setId(id);
        orderEntity.setPay_status(2);
        orderEntity.setOrder_status(201);
        orderEntity.setShopping_status(0);
        orderEntity.setPay_time(new Date());
        int num = orderService.updateOrder(orderEntity);

        if (num>0){
            return toResponseSuccess("订单修改成功");
        }else {
            return toResponsFail("订单修改失败");
        }
    }

    /**
     * 提交订单，前端提交订单后会被返回一个用户所提交的订单列表
     */
    @RequestMapping("/submit")
    public Object submit(@LoginUser UserEntity userEntity){
        Map resultObj = null;
        try {
            resultObj=orderService.submit(getJsonRequest(),userEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (resultObj!=null){
            return toResponsObject(MapUtils.getInteger(resultObj,"errno"),MapUtils.getString(resultObj,"errmsg"),resultObj.get("data"));
        }
        return toResponsFail("提交失败");
    }
    /**
     * 取消订单（然后返回前端新的订单列表）
     */
    @RequestMapping("/cancel")
    public Object cancelOrder(Integer orderId){
        OrderEntity orderEntity = orderService.queryOrderObjectById(orderId);
        if (orderEntity.getOrder_status()==300){
            return toResponsFail("您的订单已发货，无法取消订单");
        }else if (orderEntity.getOrder_status()==301){
            return toResponsFail("已收货，不能取消订单");
        }
        //退款
        if (orderEntity.getPay_status()==2){

        }
    }
}
