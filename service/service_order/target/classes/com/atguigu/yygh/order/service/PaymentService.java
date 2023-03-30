package com.atguigu.yygh.order.service;

import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.model.order.PaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PaymentService extends IService<PaymentInfo> {

    // 保存交易记录
    void savePaymentInfo(OrderInfo order,Integer paymentType);

}
