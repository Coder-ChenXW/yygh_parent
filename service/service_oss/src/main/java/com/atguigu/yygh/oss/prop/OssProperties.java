package com.atguigu.yygh.oss.prop;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "oss.file")
@PropertySource(value = {"classpath:oss.properties"})
@Data
@Component
public class OssProperties {

    private String endpoint;
    private String keyid;
    private String keysecret;
    private String bucketname;

}
