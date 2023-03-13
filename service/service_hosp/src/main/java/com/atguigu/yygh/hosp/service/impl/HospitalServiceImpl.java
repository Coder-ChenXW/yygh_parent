package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.hosp.mapper.HospitalSetMapper;
import com.atguigu.yygh.hosp.repository.HospitalRepository;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private HospitalSetMapper hospitalSetMapper;

    @Override
    public void saveHospital(Map<String, Object> resultMap) {

        Hospital hospital = JSONObject.parseObject(JSONObject.toJSONString(resultMap), Hospital.class);

        String hoscode = hospital.getHoscode();
        Hospital collection = hospitalRepository.findByHoscode(hoscode);

        if (collection == null) {

            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        } else {

            hospital.setStatus(collection.getStatus());
            hospital.setCreateTime(collection.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(collection.getIsDeleted());

            hospital.setId(collection.getId());
            hospitalRepository.save(hospital);
        }

        hospitalRepository.save(hospital);
    }

    @Override
    public String getSignKeyWithHoscode(String requestHoscode) {

        QueryWrapper<HospitalSet> hospitalSetQueryWrapper = new QueryWrapper<>();
        hospitalSetQueryWrapper.eq("hoscode", requestHoscode);

        HospitalSet hospitalSet = hospitalSetMapper.selectOne(hospitalSetQueryWrapper);

        if (hospitalSet == null) {
            throw new  YyghException(20001, "该医院信息不存在");
        }

        return hospitalSet.getSignKey();
    }

    @Override
    public Hospital getHospitalByHoscode(String hoscode) {

        return hospitalRepository.findByHoscode(hoscode);
    }

}
