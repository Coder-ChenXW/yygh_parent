package com.atguigu.yygh.user.controller.user;


import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.common.util.JwtHelper;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.user.service.PatientService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 就诊人表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-03-26
 */
@RestController
@RequestMapping("/user/userinfo/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    /**
     * @description: 增加用户就诊人
     * @author: ChenXW
     * @date: 2023-03-26 11:19
     */
    @PostMapping("/save")
    public R save(@RequestBody Patient patient, @RequestHeader String token) {
        Long userId = JwtHelper.getUserId(token);
        patient.setUserId(userId);
        patientService.save(patient);
        return R.ok();
    }


    /**
     * @description: 删除就诊人信息
     * @author: ChenXW
     * @date: 2023-03-26 11:24
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable Long id) {
        patientService.removeById(id);
        return R.ok();
    }

    /**
     * @description: 修改就诊人信息
     * @author: ChenXW
     * @date: 2023-03-26 11:25
     */
    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) {

        Patient patient = patientService.detail(id);
        return R.ok().data("patient", patient);
    }

    /**
     * @description: 更新就诊人信息
     * @author: ChenXW
     * @date: 2023-03-26 11:29
     */
    @PutMapping("/update")
    public R update(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return R.ok();
    }

    /**
     * @description: 查询就诊人信息
     * @author: ChenXW
     * @date: 2023-03-26 11:30
     */
    @GetMapping("/all")
    public R findAll(@RequestHeader String token) {



        List<Patient> list = patientService.findAll(token);

        return R.ok().data("list", list);
    }


    /**
     * @description: 根据就诊人id获取就诊人信
     * @author: ChenXW
     * @date: 2023-03-27 23:09
     */
    @GetMapping("/{patientId}")
    public Patient getPatientById(@PathVariable("patientId") Long patientId) {
        return patientService.getById(patientId);
    }

}

