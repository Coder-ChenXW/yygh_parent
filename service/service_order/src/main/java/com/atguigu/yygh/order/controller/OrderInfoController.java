package com.atguigu.yygh.order.controller;


import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-03-27
 */
@RestController
@RequestMapping("/api/order/orderInfo")
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;


    /**
     * @description: 生成订单
     * @author: ChenXW
     * @date: 2023-03-27 15:35
     */
    @PostMapping("/{scheduleId}/{patientId}}")
    public R submitOrder(@PathVariable String scheduleId,
                         @PathVariable Long patientId) {
        Long orderId =  orderInfoService.submitOrder(scheduleId, patientId);

        return R.ok().data("orderId", orderId);
    }

}

