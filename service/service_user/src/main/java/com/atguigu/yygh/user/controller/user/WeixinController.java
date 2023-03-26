package com.atguigu.yygh.user.controller.user;


import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.common.util.JwtHelper;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.prop.WeixinProperties;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.user.utils.HttpClientUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/user/userinfo/wx")
public class WeixinController {

    @Autowired
    private WeixinProperties weixinProperties;

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping("/param")
    @ResponseBody
    public R getWeixinLoginParam() throws UnsupportedEncodingException {
        String url = URLEncoder.encode(weixinProperties.getRedirecturl(), "UTF-8");
        System.out.println(weixinProperties.getRedirecturl());
        System.out.println(url);

        Map<String, Object> map = new HashMap<>();

        map.put("appid", weixinProperties.getAppid());
        map.put("scope", "snsapi_login");
        map.put("redirecturl", url);
        map.put("state", System.currentTimeMillis() + "");

        return R.ok().data(map);
    }


    @GetMapping("/callback")
    public String callback(String code, String state) throws Exception {

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder append = stringBuilder.append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");

        String format = String.format(append.toString(), weixinProperties.getAppid(), weixinProperties.getAppsecret(), code);

        String result = HttpClientUtils.get(format);
        System.out.println(result);

        JSONObject jsonObject = JSONObject.parseObject(result);
        String access_token = jsonObject.getString("access_token");
        String openid = jsonObject.getString("openid");

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);


        UserInfo userInfo = userInfoService.getOne(queryWrapper);
        if (userInfo == null) { // 首次使用微信登录

            StringBuilder sb = new StringBuilder();
            StringBuilder append1 = sb.append("https://api.weixin.qq.com/sns/userinfo")
                    .append("?access_token=%s")
                    .append("&openid=%s")
                    .append("&lang=zh_CN");
            String format1 = String.format(sb.toString(), access_token, openid);
            String s = HttpClientUtils.get(format1);
            JSONObject jsonObject1 = JSONObject.parseObject(s);
            String nickname = jsonObject1.getString("nickname");

            userInfo = new UserInfo();
            userInfo.setOpenid(openid);
            userInfo.setNickName(nickname);
            userInfo.setStatus(1);
            userInfoService.save(userInfo);
        }

        if (userInfo.getStatus() == 0) {
            throw new YyghException(20001, "用户被禁用");
        }

        Map<String, String> map = new HashMap<>();


        if (StringUtils.isEmpty(userInfo.getPhone())) {
            map.put("openid", openid);
        } else {
            map.put("openid", "");
        }

        String name = userInfo.getName();
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name", name);

        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);


        return "redirect:http://localhost:3000/weixin/callback?token=" + map.get("token") + "&openid=" + map.get("openid")+ "&name=" + URLEncoder.encode(map.get("name"), "UTF-8");
    }


}
