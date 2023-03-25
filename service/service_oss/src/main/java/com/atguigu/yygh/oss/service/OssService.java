package com.atguigu.yygh.oss.service;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.atguigu.yygh.oss.prop.OssProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
public class OssService {

    @Autowired
    private OssProperties ossProperties;


    public String upload(MultipartFile file) {

        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = ossProperties.getEndpoint();
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ossProperties.getKeyid();
        String accessKeySecret = ossProperties.getKeysecret();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ossProperties.getBucketname();
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
//        String objectName = "exampledir/exampleobject.txt";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String fileName = new DateTime().toString("yyyy/MM/dd") + UUID.randomUUID().toString().replaceAll("-", "") + file.getOriginalFilename();

        try {
            ossClient.putObject(bucketName, fileName, file.getInputStream());
            // https://tanhuacouldplatform.oss-cn-beijing.aliyuncs.com
            return "https://" +ossProperties.getBucketname() + "." + ossProperties.getEndpoint() + "/" + fileName;
        } catch (Exception ce) {
            System.out.println(ce.getMessage());
            return null;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

}
