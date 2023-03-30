package com.atguigu.yygh.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.enums.OrderStatusEnum;
import com.atguigu.yygh.enums.PaymentStatusEnum;
import com.atguigu.yygh.enums.PaymentTypeEnum;
import com.atguigu.yygh.enums.RefundStatusEnum;
import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.model.order.PaymentInfo;
import com.atguigu.yygh.model.order.RefundInfo;
import com.atguigu.yygh.order.prop.WeiPayProperties;
import com.atguigu.yygh.order.service.OrderInfoService;
import com.atguigu.yygh.order.service.PaymentService;
import com.atguigu.yygh.order.service.RefundInfoService;
import com.atguigu.yygh.order.service.WeiPayService;
import com.atguigu.yygh.order.utils.HttpClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class WeiPayServiceImpl implements WeiPayService {


    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private WeiPayProperties weiPayProperties;

    @Autowired
    private RefundInfoService refundInfoService;

    @Override
    public String createNative(Long orderId) {

        OrderInfo orderInfo = orderInfoService.getById(orderId);

        paymentService.savePaymentInfo(orderInfo, PaymentTypeEnum.WEIXIN.getStatus());

        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", weiPayProperties.getAppid());
        paramMap.put("mch_id", weiPayProperties.getPartner());
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());

        Date reserveDate = orderInfo.getReserveDate();
        String reserveDateString = new DateTime(reserveDate).toString("yyyy-MM-dd");
        String body = reserveDateString + "就诊" + orderInfo.getDepname();

        paramMap.put("body", body);
        paramMap.put("out_trade_no", orderInfo.getOutTradeNo());
        paramMap.put("total_fee", "1");
        paramMap.put("spbill_create_ip", "127.0.0.1");
        paramMap.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");
        paramMap.put("trade_type", "NATIVE");


        try {
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(paramMap, weiPayProperties.getPartnerkey()));
            httpClient.setHttps(true); // 设置https
            httpClient.post(); // 发送请求

            String xmlResult = httpClient.getContent();// 获取返回结果
            Map<String, String> stringStringMap = WXPayUtil.xmlToMap(xmlResult);
//            System.out.println(stringStringMap);
            return stringStringMap.get("code_url");
        } catch (Exception ex) {
//            throw new YyghException(20001, "微信支付失败");
            return "";
        }
    }

    @Override
    public Map<String, String> queryPayStatus(Long orderId) {

        OrderInfo orderInfo = orderInfoService.getById(orderId);

        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
        Map<String, String> map = new HashMap<>();

        map.put("appid", weiPayProperties.getAppid());
        map.put("mch_id", weiPayProperties.getPartner());
        map.put("transaction_id", weiPayProperties.getAppid());

        map.put("out_trade_no", orderInfo.getOutTradeNo()); // 商户订单号
        map.put("nonce_str", WXPayUtil.generateNonceStr()); // 随机字符串

        try {
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(map, weiPayProperties.getPartnerkey()));
            httpClient.setHttps(true); // 设置https
            httpClient.post(); // 发送请求
            String content = httpClient.getContent();// 获取返回结果
            Map<String, String> stringStringMap = WXPayUtil.xmlToMap(content);

            return stringStringMap;
        } catch (Exception ex) {

            return null;
        }

    }


    @Transactional
    @Override
    public void paySuccess(Long orderId, Map<String, String> map) {
        // 更新订单状态
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(orderId);
        orderInfo.setOrderStatus(OrderStatusEnum.PAID.getStatus());
        orderInfoService.updateById(orderInfo);
        // 更新支付记录表
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("order_id", orderId);
        updateWrapper.set("trade_no", map.get("transaction_id"));  // 微信支付订单号
        updateWrapper.set("payment_status", PaymentStatusEnum.PAID.getStatus()); // 支付状态
        updateWrapper.set("callback_time", new DateTime()); // 回调时间
        updateWrapper.set("callback_content", JSONObject.toJSONString(map)); // 回调内容
        paymentService.update(updateWrapper);
    }

    @Override
    public boolean refund(Long orderId) {

        QueryWrapper<PaymentInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("order_id", orderId);
        PaymentInfo paymentInfo = paymentService.getOne(queryWrapper);

        RefundInfo refundInfo = refundInfoService.saveRefundInfo(paymentInfo);

        // 已退款
        if (refundInfo.getRefundStatus().intValue() == RefundStatusEnum.REFUND.getStatus().intValue()) {
            return true;
        }

        // 执行退款
        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/secapi/pay/refund");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", weiPayProperties.getAppid());
        paramMap.put("mch_id", weiPayProperties.getPartner());
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        paramMap.put("transaction_id", paymentInfo.getTradeNo());
        paramMap.put("out_trade_no", paymentInfo.getOutTradeNo());
        paramMap.put("out_refund_no", "tk" + refundInfo.getOutTradeNo());

        paramMap.put("total_fee", "1");
        paramMap.put("refund_fee", "1");

        try {
            String paramXml = WXPayUtil.generateSignedXml(paramMap, weiPayProperties.getPartnerkey());
            httpClient.setXmlParam(paramXml);
            httpClient.setHttps(true); // 设置https
            httpClient.setCert(true); // 设置证书
            httpClient.setCertPassword(weiPayProperties.getPartner()); // 设置证书密码
            httpClient.post(); // 发送请求

            String content = httpClient.getContent();// 获取返回结果
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);
            if ("SUCCESS".equals(resultMap.get("result_code"))) {
                refundInfo.setTradeNo(resultMap.get("refund_id"));
                refundInfo.setRefundStatus(RefundStatusEnum.REFUND.getStatus());
                refundInfo.setCallbackTime(new Date());
                refundInfo.setCallbackContent(JSONObject.toJSONString(resultMap));
                refundInfoService.updateById(refundInfo);
                return true;
            }
            return false;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

}
