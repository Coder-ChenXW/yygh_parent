package com.atguigu.yygh.hosp.controller;


import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.model.acl.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/user")
@CrossOrigin
public class UserController {

    @PostMapping(value = "/login")
    public R login(@RequestBody User user) {

        return R.ok().data("token", "admin-token");

    }

    @GetMapping(value = "/info")
    public R info(String token) {
        return R.ok().data("token","[admin]")
                .data("introduction","I am a super administrator")
                .data("avatar","https://img1.baidu.com/it/u=693355708,2712228411&fm=253&fmt=auto&app=138&f=JPEG?w=640&h=418")
                .data("name","Super Admin");
    }

}
