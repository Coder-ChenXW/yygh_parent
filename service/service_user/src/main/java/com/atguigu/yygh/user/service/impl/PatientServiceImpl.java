package com.atguigu.yygh.user.service.impl;


import com.atguigu.yygh.client.DictFeignClient;
import com.atguigu.yygh.common.util.JwtHelper;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.user.mapper.PatientMapper;
import com.atguigu.yygh.user.service.PatientService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 就诊人表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-03-26
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public List<Patient> findAll(String token) {

        Long userId = JwtHelper.getUserId(token);
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);

        List<Patient> patients = baseMapper.selectList(queryWrapper);
        patients.stream().forEach(item -> {
            this.packagePatient(item);
        });

        return patients;
    }

    private void packagePatient(Patient item) {
        item.getParam().put("certificatesTypeString", dictFeignClient.getNameByValue(Long.parseLong(item.getCertificatesType())));
    }

}
