package org.example.controller;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.example.Util.CharUtil;
import org.example.Util.DateTool;
import org.example.Util.ResourceTool;
import org.example.annotations.LoginUser;
import org.example.entity.OrderEntity;
import org.example.entity.OrderGoodsEntity;
import org.example.entity.UserEntity;
import org.example.service.OrderGoodsService;
import org.example.service.OrderService;
import org.example.utils.ApiBaseAction;
import org.example.utils.MapUtils;
import org.example.utils.XmlUtils;
import org.example.utils.weChat.WeChatConfig;
import org.example.utils.weChat.WeChatTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: houlintao
 * @Date:2020/6/23 下午6:05
 * @email 437547058@qq.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/pay")
public class PayController extends ApiBaseAction {

    private Logger logger = Logger.getLogger(PayController.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderGoodsService orderGoodsService;

    /**
     * 预支付，商户系统（即你的系统）先调用微信平台提供的统一下单接口在微信支付服务后台生成预支付交易单，
     * 待返回正确的预支付交易会话标志后再在APP里发起支付；
     * 接口url地址：https://api.mch.weixin.qq.com/pay/unifiedorder
     * 微信平台返回结果：
     * return_code 返回状态码，例如SUCCESSS；
     * return_msg 返回信息，例如“签名成功”
     */
    @PostMapping("/prePay")
    public Object payPrePay(@LoginUser UserEntity loginUser,Integer orderId) throws IOException, DocumentException {
        //通过orderId查询order实体
        OrderEntity orderEntity = orderService.queryOrderObjectById(orderId);

        if (orderEntity == null){
            return toResponsObject(400,"订单已取消","");
        }

        if (orderEntity.getOrder_status()!=0){
            return toResponsObject(400,"请不要重复支付","");
        }

        //生成一个随机字符串
        String nonceStr = CharUtil.getRandomString(32);
        //使用一个TreeMap容器持有待返回的结果
        /**
         * TreeMap依靠红黑树实现，是有序的，取出的时候也是有序的；；而HashMap是无序的；
         * TreeMap适用于按照自然顺序或者自定义的顺序遍历key；
         */
        Map<Object,Object> resultObj = new TreeMap();
        //使用TreeMap持有对微信支付的请求参数
        Map<Object,Object> wxPayParam = new TreeMap<Object, Object>();
        wxPayParam.put("appid", ResourceTool.getConfigPropertyByName("wx.appId"));
        //商户号 是微信给每个商户的识别代码
        wxPayParam.put("mch_id",ResourceTool.getConfigPropertyByName("wx.mchId"));

        String randomStr = CharUtil.getRandomString(18).toUpperCase();
        wxPayParam.put("nonce_str",randomStr);
        //商户订单号，即第三方平台的订单号，这里使用当前订单对象实体的序列号
        wxPayParam.put("out_trade_no",orderEntity.getOrder_sn());
        wxPayParam.put("order_id",orderId);
        wxPayParam.put("body","收礼簿-支付");

        Map orderGoodsParam = new HashMap();
        List<OrderGoodsEntity> orderGoods = orderGoodsService.queryList(orderGoodsParam);

        if (orderGoods != null){
            String body="收礼簿—";
            for (OrderGoodsEntity orderGood:orderGoods){
                body = body+orderGood.getGoods_name()+",";
            }
            if (body.length()>0){
                body = body.substring(0,body.length()-1);
            }
            wxPayParam.put("body",body);
        }
        //支付总金额
        wxPayParam.put("total_fee",orderEntity.getActual_price().multiply(new BigDecimal(100)).intValue());
        /**
         * 回调地址，接收微信支付异步通知回调地址，此url必须为直接可访问的url且不能携带参数；
         */
        wxPayParam.put("notify_url",ResourceTool.getConfigPropertyByName("wx.notifyUrl"));
        // 交易类型APP
        wxPayParam.put("trade_type",ResourceTool.getConfigPropertyByName("wx.tradeType"));
        //终端ip，调用微信支付api的机器ip
        wxPayParam.put("spbill_create_ip",getClientIp());

        //数字签名,"wx.paySignKey"为微信支付i签名
        String sign = WeChatTool.arraySign(wxPayParam,ResourceTool.getConfigPropertyByName("wx.paySignKey"));
        wxPayParam.put("sign",sign);

        //把TreeMap的 wxPayParam转换为字符串
        String xmlStr = MapUtils.convertMap2Xml(wxPayParam);

        //请求微信支付统一下单接口，使用xmlStr作为请求参数执行微信支付
        String requestOnceResult = WeChatTool.requestOnce(ResourceTool.getConfigPropertyByName("wx.uniformorder"), xmlStr);

        //将微信支付返回的结果字符串转换为Map
        Map<String, Object> wxReturnMap = XmlUtils.xmlStrToMap(requestOnceResult);

        //获取微信平台的返回状态码
        String return_code = MapUtils.getString("return_code",wxReturnMap);
        String return_msg = MapUtils.getString("return_msg",wxReturnMap);

        if (return_code.equalsIgnoreCase("FAIL")){
            return toResponsFail("支付失败:"+return_msg);
        }else if (return_code.equalsIgnoreCase("SUCCESS")){
            /**
             * 当返回的return_code为SUCCESS的时候微信平台会返回appid，设备id，商户号，随机字符串，
             * result_code（业务结果），err_code_dec(错误代码描述)等数据；
             */
            String result_code = MapUtils.getString("return_code",wxReturnMap);
            //获取微信平台返回的错误代码描述
            String err_code_des = MapUtils.getString("err_code_des",wxReturnMap);

            if (result_code.equalsIgnoreCase("FAIL")){
                return toResponsFail("支付失败，"+err_code_des);
            }else if (result_code.equalsIgnoreCase("SUCCESS")){
                /**
                 * 在当return_code和result_code的值都是SUCCESS时会返回
                 * 预支付交易绘画标志prepay_id；
                 */
                String prePay_id = MapUtils.getString("prepay_id",wxReturnMap);

                //设置返回对象
                resultObj.put("appId",ResourceTool.getConfigPropertyByName("wx.appId"));
                resultObj.put("timeStamp", DateTool.timeToString(System.currentTimeMillis()/1000,DateTool.DATE_TIME_PATTERN));
                resultObj.put("nonceStr",nonceStr);
                resultObj.put("package", "prepay_id=" + prePay_id);
                resultObj.put("signType", "MD5");
                //支付签名
                String paySign = WeChatTool.arraySign(resultObj,ResourceTool.getConfigPropertyByName("wx.paySignKey"));
                resultObj.put("paySign", paySign);

                orderEntity.setPay_id(prePay_id);
                orderEntity.setPay_status(1);

                orderService.updateOrder(orderEntity);
            }
        }
        return toResponsObject(0,"微信统一下单支付成功",resultObj);
    }
}
