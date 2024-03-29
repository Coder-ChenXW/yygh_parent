package com.atguigu.yygh.hosp.controller.api;


import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.util.MD5;
import com.atguigu.yygh.hosp.bean.Result;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.utils.HttpRequestHelper;
import com.atguigu.yygh.model.hosp.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@RequestMapping("/api/hosp")
public class ApiHospitalController {


    @Autowired
    private HospitalService hospitalService;

    @PostMapping("/hospital/show")
    public Result<Hospital> getHospitalInfo(HttpServletRequest request) {
//        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> stringObjectMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = (String) stringObjectMap.get("hoscode");


        Hospital hospital = hospitalService.getHospitalByHoscode(hoscode);

        return Result.ok(hospital);
    }

    @PostMapping("/saveHospital")
    public Result saveHospital(HttpServletRequest request) {

        Map<String, Object> resultMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String requestSignKey = (String) resultMap.get("sign");
        String requestHoscode = (String) resultMap.get("hoscode");

        String platformSignKey = hospitalService.getSignKeyWithHoscode(requestHoscode);

        String encrypt = MD5.encrypt(platformSignKey);

        if (!StringUtils.isEmpty(requestSignKey) && !StringUtils.isEmpty(encrypt) && encrypt.equals(requestSignKey)) {
            String logoData = (String) resultMap.get("logoData");
            String result = logoData.replaceAll(" ", "+");
            resultMap.put("logoData", result);

            hospitalService.saveHospital(resultMap);
            return Result.ok();
        } else {
            throw new YyghException(20001, "保存失败");
        }


    }


}
