package com.atguigu.yygh.task.job;


import com.atguigu.yygh.mq.MqConst;
import com.atguigu.yygh.mq.RabbitService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PatientRemindJob {

    @Autowired
    private RabbitService rabbitService;

    @Scheduled(cron = "*/30 * * * * *")
    public void printTime() {
        System.out.println(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));

        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_TASK, MqConst.ROUTING_TASK_8, " ");


    }

}
