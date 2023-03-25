package com.atguigu.yygh.sms.controller;


import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user/sms")
public class SMSController {

    @Autowired
    private SmsService smsService;

    /**
     * @description: 发送短信验证码
     * @author: ChenXW
     * @date: 2023-03-23 10:00
     */
    @PostMapping("/send/{phone}")
    public R sendCode(@PathVariable String phone) {
        boolean flag = smsService.sendCode(phone);
        if (flag) {
            return R.ok();
        } else {
            return R.error().message("发送短信失败");
        }
    }

}
