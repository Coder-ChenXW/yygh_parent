package com.atguigu.yygh.hosp.controller.user;


import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/hosp/hospital")
public class UserHospitalController {

    @Autowired
    private HospitalService hospitalService;


    /**
     * @description: 获取医院列表
     * @author: ChenXW
     * @date: 2023-03-20 10:32
     */
    @GetMapping("/list")
    public R getHospitalList(HospitalQueryVo hospitalQueryVo) {

        Page<Hospital> page = hospitalService.getHospitalPage(1, 1000000, hospitalQueryVo);

        return R.ok().data("list", page.getContent());
    }


    /**
     * @description: 根据医院名称查询医院列表(模糊查询) 用于预约挂号页面的医院列表显示
     * @author: ChenXW
     * @date: 2023-03-20 10:32
     */
    @GetMapping("/{name}")
    public R findByName(@PathVariable String name) {

        List<Hospital> list = hospitalService.findByNameLike(name);

        return R.ok().data("list", list);
    }


    /**
     * @description: 查询医院的详情信息
     * @author: ChenXW
     * @date: 2023-03-20 11:16
     */
    @GetMapping("/detail/{hoscode}")
    public R getHospitalDetail(@PathVariable String hoscode) {

        Hospital hospital = hospitalService.getHospitalDetail(hoscode);

        return R.ok().data("hospital", hospital);

    }

}
