package com.atguigu.yygh.sms.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.yygh.sms.service.SmsService;
import com.atguigu.yygh.sms.utils.RandomUtil;
import com.google.gson.Gson;
import java.util.concurrent.TimeUnit;

import com.aliyuncs.dysmsapi.model.v20170525.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean sendCode(String phone) {

        String redisCode = (String) redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(redisCode)) {
            return true;
        }

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI5tCCK6DfMXZFPZxZB6qN", "ZgBBhd015DUNAcMgCmM5ILHlkMvyR9");
        /** use STS Token
         DefaultProfile profile = DefaultProfile.getProfile(
         "<your-region-id>",           // The region ID
         "<your-access-key-id>",       // The AccessKey ID of the RAM account
         "<your-access-key-secret>",   // The AccessKey Secret of the RAM account
         "<your-sts-token>");          // STS Token
         **/

        IAcsClient client = new DefaultAcsClient(profile);

        String fourBitRandom = RandomUtil.getFourBitRandom();
        System.out.println("验证码：" + fourBitRandom);


        SendSmsRequest request = new SendSmsRequest();
        request.setSignName("tanhua物流云管平台");
//        request.setSignName("麻豆传媒");
        request.setTemplateCode("SMS_274545467");
        request.setPhoneNumbers(phone);
//        request.setTemplateParam("{\"code\":\"1234\"}");
        request.setTemplateParam("{\"code\":\"" + fourBitRandom + "\"}");

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));


            redisTemplate.opsForValue().set(phone, fourBitRandom, 15, TimeUnit.DAYS);

            return true;

        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }

        return false;
    }


    public static void main(String[] args) {
        new SmsServiceImpl().sendCode("18582124521");
//        new SmsServiceImpl().sendCode("18383936872");
//        18383936872
    }

}
