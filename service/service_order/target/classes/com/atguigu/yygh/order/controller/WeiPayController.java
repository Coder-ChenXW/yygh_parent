package com.atguigu.yygh.order.controller;


import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.enums.OrderStatusEnum;
import com.atguigu.yygh.enums.PaymentStatusEnum;
import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.order.service.OrderInfoService;
import com.atguigu.yygh.order.service.PaymentService;
import com.atguigu.yygh.order.service.WeiPayService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user/order/weixin")
public class WeiPayController {


    @Autowired
    private WeiPayService weiPayService;




    /**
     * @description: 查询订单状态
     * @author: ChenXW
     * @date: 2023-03-29 17:49
     */
    @GetMapping("/status/{orderId}")
    public R getPayStatus(@PathVariable Long orderId) {
        Map<String, String> map = weiPayService.queryPayStatus(orderId);
        if (map == null) {
            return R.error().message("支付失败");
        }

        if ("SUCCESS".equals(map.get("trade_state"))) { //支付成功
            weiPayService.paySuccess(orderId, map);

            return R.ok();
        }

        return R.ok().message("支付中"); // 支付失败

    }


    /**
     * @description: 生成微信支付二维码
     * @author: ChenXW
     * @date: 2023-03-29 16:20
     */
    @GetMapping("/{orderId}")
    public R createNative(@PathVariable Long orderId) {
        String url = weiPayService.createNative(orderId);
        return R.ok().data("url", url);
    }

}
