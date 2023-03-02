package com.atguigu.yygh.hosp;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "com.atguigu.yygh")
@MapperScan(value = "com.atguigu.yygh.hosp.mapper")
public class ServiceHospMainStarter {

    public static void main(String[] args) {
        SpringApplication.run(ServiceHospMainStarter.class,args);
    }

}
