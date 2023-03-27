package com.atguigu.yygh.user.controller.admin;


import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/administrator/userinfo")
public class AdminUserInfoController {

    @Autowired
    private UserInfoService userInfoService;


    /**
     * @description: 认证通过
     * @author: ChenXW
     * @date: 2023-03-27 9:46
     */
    @PutMapping("/auth/{id}/{authStatus}")
    public R approval(@PathVariable Long id,
                          @PathVariable Integer authStatus) {

        if (authStatus ==2 || authStatus ==- 1) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(id);
            userInfo.setAuthStatus(authStatus);
            userInfoService.updateById(userInfo);
        }

        return R.ok();
    }


    /**
     * @description: 查看用户详情
     * @author: ChenXW
     * @date: 2023-03-27 9:00
     */
    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) {
        Map<String, Object> map = userInfoService.detail(id);
        return R.ok().data(map);
    }


    /**
     * @description: 修改用户状态
     * @author: ChenXW
     * @date: 2023-03-27 8:53
     */
    @PutMapping("/{id}/{status}")
    public R updateStatus(@PathVariable Long id,
                          @PathVariable Integer status) {
        userInfoService.updateStatus(id, status);
        return R.ok();
    }

    @GetMapping("/{pageNum}/{limit}")
    public R getUserInfoPage(@PathVariable Integer pageNum,
                             @PathVariable Integer limit,
                             UserInfoQueryVo userInfoQueryVo) {

        Page<UserInfo> page = userInfoService.getUserInfoPage(pageNum, limit, userInfoQueryVo);

        return R.ok().data("page", page.getTotal()).data("list", page.getRecords());
    }

}
