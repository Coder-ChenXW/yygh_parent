package com.atguigu.yygh.hosp.controller.admin;


import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hospital")
public class HospitalController {


    @Autowired
    private HospitalService hospitalService;


    /**
     * @description: 根据医院id查询医院信息
     * @author: ChenXW
     * @date: 2023-03-16 19:27
     */
    @GetMapping("/detail/{id}")
    public R detail(@PathVariable String id) {
        Hospital hospital = hospitalService.detail(id);
        return R.ok().data("hospital", hospital);
    }


    /**
     * @description: 根据医院id修改医院状态
     * @author: ChenXW
     * @date: 2023-03-16 13:38
     */
    @PutMapping("/{id}/{status}")
    public R updateStatus(@PathVariable String id, @PathVariable Integer status) {
        hospitalService.updateStatus(id, status);
        return R.ok();
    }


    /**
     * @description: 分页展示
     * @author: ChenXW
     * @date: 2023-03-15 9:40
     */
    @GetMapping("/{pageNum}/{pageSize}")
    public R getHospitalPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, HospitalQueryVo hospitalQueryVo) {

        Page<Hospital> hospitalPage = hospitalService.getHospitalPage(pageNum, pageSize, hospitalQueryVo);

        return R.ok().data("total", hospitalPage.getTotalElements()).data("list", hospitalPage.getContent());
    }

}
