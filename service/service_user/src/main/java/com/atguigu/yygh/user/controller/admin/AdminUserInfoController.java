package com.atguigu.yygh.user.controller.admin;


import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/administrator/userinfo")
public class AdminUserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/{pageNum}/{limit}")
    public R getUserInfoPage(@PathVariable Integer pageNum,
                             @PathVariable Integer limit,
                             UserInfoQueryVo userInfoQueryVo) {

        Page<UserInfo> page = userInfoService.getUserInfoPage(pageNum, limit, userInfoQueryVo);

        return R.ok().data("page", page.getTotal()).data("list", page.getRecords());
    }

}
