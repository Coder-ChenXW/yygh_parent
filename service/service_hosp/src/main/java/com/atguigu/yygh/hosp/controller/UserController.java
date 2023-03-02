package com.atguigu.yygh.hosp.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/abc/xxx")
public class UserController {

    @GetMapping("/info")
    public String hello(){
        return "hello world";
    }

}
