package com.atguigu.yygh.hosp.controller;


import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 医院设置表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-03-02
 */
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    @GetMapping(value = "/findAll")
    public List<HospitalSet> getHospitalSetList() {
        return hospitalSetService.list();
    }


    // 根据医院设置id删除医院设置信息
    @DeleteMapping(value = "/deleteById/{id}")
    public boolean deleteById(@PathVariable Integer id) {
        return hospitalSetService.removeById(id);
    }

}

