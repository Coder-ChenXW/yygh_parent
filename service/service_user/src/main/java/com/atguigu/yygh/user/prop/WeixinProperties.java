package com.atguigu.yygh.user.prop;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "weixin")
@Data
public class WeixinProperties {

    private String appid;
    private String appsecret;
    private String redirecturl;

}
