package com.atguigu.yygh.user.service.impl;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.util.JwtHelper;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.mapper.UserInfoMapper;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.LoginVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-03-20
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {


    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public Map<String, Object> login(LoginVo loginVo) {

        String phone = loginVo.getPhone();
        String code = loginVo.getCode();

        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            throw new YyghException(20001, "手机号或验证码有误");
        }

        // 对验证码做进一步的校验
        String redisCode = (String) redisTemplate.opsForValue().get(phone);
        if (StringUtils.isEmpty(redisCode) || !redisCode.equals(code)) {
            throw new YyghException(20001, "验证码有误");
        }

        String openid = loginVo.getOpenid();
        UserInfo userInfo = null;
        if (StringUtils.isEmpty(openid)) {
            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone", phone);
            userInfo = baseMapper.selectOne(queryWrapper);
            if (userInfo == null) {
                userInfo = new UserInfo();
                userInfo.setPhone(phone);
                baseMapper.insert(userInfo);
                userInfo.setStatus(1);
            }


        } else { // 微信强制绑定手机号
            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("openid", openid);
            userInfo = baseMapper.selectOne(queryWrapper);

            QueryWrapper<UserInfo> phoneWrapper = new QueryWrapper<>();
            phoneWrapper.eq("phone", phone);
            UserInfo userInfo2 = baseMapper.selectOne(phoneWrapper);

            if (userInfo2 != null) {
                userInfo.setPhone(phone);
//                userInfo.setStatus(1);
                baseMapper.updateById(userInfo);
            } else {
                userInfo2.setOpenid(userInfo.getOpenid());
                userInfo2.setNickName(userInfo.getNickName());
                baseMapper.updateById(userInfo2);
                baseMapper.deleteById(userInfo.getId());
            }
        }

        if (userInfo.getStatus() == 0) {
            throw new YyghException(20001, "用户被禁用");
        }

        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();

        String token = JwtHelper.createToken(userInfo.getId(), name);

        if (StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }

        map.put("name", name);
//        map.put("token", "");
        map.put("token", token);

//        Map<String, Object> result = new HashMap<>();
//        String name = userInfo.getName();
//        if (!StringUtils.isEmpty(name)) {
//            result.put("name", name);
//        }
//        if (StringUtils.isEmpty(result.get("name"))) {
//            result.put("name", userInfo.getNickName());
//        }
//        if (StringUtils.isEmpty(result.get("name"))) {
//            result.put("name", userInfo.getPhone());
//        }
//
//        result.put("token", userInfo.getPhone());

        return map;
    }
}
