package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.client.DictFeignClient;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.enums.DictEnum;
import com.atguigu.yygh.hosp.mapper.HospitalSetMapper;
import com.atguigu.yygh.hosp.repository.HospitalRepository;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private HospitalSetMapper hospitalSetMapper;

    @Autowired
    private DictFeignClient dictFeignClient;

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
            throw new YyghException(20001, "该医院信息不存在");
        }

        return hospitalSet.getSignKey();
    }

    @Override
    public Hospital getHospitalByHoscode(String hoscode) {

        return hospitalRepository.findByHoscode(hoscode);
    }

    @Override
//    public Page<Hospital> getHospitalPage(Integer pageNum, Integer pageSize, HospitalQueryVo hospitalQueryVo) {
    public Page<Hospital> getHospitalPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {

//        Hospital hospital = new Hospital();
//        if (!StringUtils.isEmpty(hospitalQueryVo.getHosname())) {
//            hospital.setHosname(hospitalQueryVo.getHosname());
//        }
//
//        if (!StringUtils.isEmpty(hospitalQueryVo.getProvinceCode())) {
//            hospital.setProvinceCode(hospitalQueryVo.getProvinceCode());
//        }
//
//        if (!StringUtils.isEmpty(hospitalQueryVo.getCityCode())) {
//            hospital.setCityCode(hospitalQueryVo.getCityCode());
//        }
//
//        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withMatcher("hosname", ExampleMatcher.GenericPropertyMatchers.contains())
//                .withIgnoreCase(true);
//
//        Example<Hospital> of = Example.of(hospital, matcher);
//
//        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, Sort.by("createTime").ascending());
//
//        Page<Hospital> page = hospitalRepository.findAll(of, pageRequest);
//
//        page.getContent().stream().forEach(item -> {
//
//            this.packageHospital(hospital);
//
//        });
//
//
//        return page;

        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //0为第一页
        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象

                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写

        //创建实例
        Example<Hospital> example = Example.of(hospital, matcher);

        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);

        //封装医院等级数据
        pages.getContent().stream().forEach(item -> {

//            this.packHospital(item);
            this.packageHospital(item);
        });

        return pages;

    }

    @Override
    public void updateStatus(String id, Integer status) {

        if (status == 0 || status == 1) {

            Hospital hospital = hospitalRepository.findById(id).get();

            hospital.setStatus(status);

            hospital.setUpdateTime(new Date());

            hospitalRepository.save(hospital);

        }

    }

    @Override
    public Hospital detail(String id) {

        Hospital hospital = hospitalRepository.findById(id).get();

        this.packageHospital(hospital);

        return hospital;
    }

    @Override
    public List<Hospital> findByNameLike(String name) {
        return hospitalRepository.findByHosnameLike(name);
    }

    @Override
    public Hospital getHospitalDetail(String hoscode) {

        Hospital hospital = hospitalRepository.findByHoscode(hoscode);

        this.packageHospital(hospital);

        return hospital;
    }


    private void packageHospital(Hospital hospital) {

        String hostype = hospital.getHostype();
        String provinceCode = hospital.getProvinceCode();
        String cityCode = hospital.getCityCode();
        String districtCode = hospital.getDistrictCode();

        String provinceAddress = dictFeignClient.getNameByValue(Long.parseLong(provinceCode));
        String cityAddress = dictFeignClient.getNameByValue(Long.parseLong(cityCode));
        String districtAddress = dictFeignClient.getNameByValue(Long.parseLong(districtCode));

        String level = dictFeignClient.getNameByDictCodeAndValue(DictEnum.HOSTYPE.getDictCode(), Long.parseLong(hostype));

        hospital.getParam().put("level", level);
        hospital.getParam().put("address", provinceAddress + cityAddress + districtAddress + hospital.getAddress());

        hospital.getParam().put("hosTypeString", level);
        hospital.getParam().put("fullAddress", provinceAddress + cityAddress + districtAddress + hospital.getAddress());


    }


}
