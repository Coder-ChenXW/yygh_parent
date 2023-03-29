package com.atguigu.yygh.mq;

public class MqConst {

    /**
     * 预约下单
     */
    public static final String EXCHANGE_DIRECT_ORDER = "xxx";
    public static final String ROUTING_ORDER = "dddd";
    //队列
    public static final String QUEUE_ORDER = "yyy";
    /**
     * 短信
     */
    public static final String EXCHANGE_DIRECT_SMS = "exchange.direct.msm";
    public static final String ROUTING_MSM_ITEM = "msm.item";
    //队列
    public static final String QUEUE_MSM_SMS = "queue.msm.item";

}
