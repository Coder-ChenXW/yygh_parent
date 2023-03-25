package com.atguigu.yygh.user.controller;


import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.common.util.JwtHelper;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-03-20
 */
@RestController
@RequestMapping("/user/userinfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * @description: 用户登录
     * @author: ChenXW
     * @date: 2023-03-20 16:08
     */
    @PostMapping("/login")
    public R login(@RequestBody LoginVo loginVo) {
        Map<String, Object> map = userInfoService.login(loginVo);
        return R.ok().data(map);
    }

    @GetMapping("/info")
    public R getUserInfo(@RequestHeader String token) {
        Long userId = JwtHelper.getUserId(token);
        UserInfo byId = userInfoService.getUserInfo(userId);



        return R.ok().data("user", byId);
    }

}

