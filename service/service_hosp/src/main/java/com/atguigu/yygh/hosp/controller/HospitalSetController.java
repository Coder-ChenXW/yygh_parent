package com.atguigu.yygh.hosp.controller;


import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.common.util.MD5;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * <p>
 * 医院设置表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-03-02
 */
@CrossOrigin
@RestController
@Api(tags = "医院设置信息")
@RequestMapping("/admin/hosp/hospitalSet")
@Slf4j
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;


    // 锁定与解锁
    @PutMapping("/status/{id}/{status}")
    public R updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        log.info("current thread is: " + Thread.currentThread().getId() + ",params:id=" + id);

        HospitalSet hospitalSet = new HospitalSet();
        hospitalSet.setId(id);
        hospitalSet.setStatus(status);
        hospitalSetService.updateById(hospitalSet);
//        HospitalSet byId = hospitalSetService.getById(id);

        log.info("result: " + Thread.currentThread().getId() + hospitalSet.toString());
        return R.ok();
    }

    // 批量删除
    @DeleteMapping("/delete")
    public R batchDelete(@RequestBody List<Integer> ids) {
        hospitalSetService.removeByIds(ids);
        return R.ok();
    }

    // 修改回显数据
    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Integer id) {
        return R.ok().data("item", hospitalSetService.getById(id));
    }

    // 修改修改数据
    @PutMapping("/update")
    public R update(@RequestBody HospitalSet hospitalSet) {
        hospitalSetService.updateById(hospitalSet);
        return R.ok();
    }


    @ApiOperation(value = "新增接口")
    @PostMapping("/save")
    public R save(@RequestBody HospitalSet hospitalSet) {
        hospitalSet.setStatus(1);
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));
        hospitalSetService.save(hospitalSet);

        return R.ok();
    }


//    @ApiOperation(value = "带查询条件的分页")
//    @GetMapping(value = "/page/{currentNum}/{size}")

    @ApiOperation(value = "分页条件医院设置列表")
    @PostMapping("/page/{page}/{limit}")
    public R pageQuery(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "hospitalSetQueryVo", value = "查询对象", required = false)
            @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        Page<HospitalSet> pageParam = new Page<>(page, limit);
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        if (hospitalSetQueryVo == null) {
            hospitalSetService.page(pageParam, queryWrapper);
        } else {
            String hosname = hospitalSetQueryVo.getHosname();
            String hoscode = hospitalSetQueryVo.getHoscode();
            if (!StringUtils.isEmpty(hosname)) {
                queryWrapper.like("hosname", hosname);
            }
            if (!StringUtils.isEmpty(hoscode)) {
                queryWrapper.eq("hoscode", hoscode);
            }
            hospitalSetService.page(pageParam, queryWrapper);
        }
        List<HospitalSet> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation(value = "查询所有的医院设置信息")
    @GetMapping(value = "/findAll")
    public R findAll() {
//        List<HospitalSet> list = null;
//        try {
//            list = hospitalSetService.list();
//        } catch (Exception ex) {
//            throw new YyghException(200013, "Yygh异常");
//        }

        List<HospitalSet> list = hospitalSetService.list();

        return R.ok().data("items", list);
    }


    // 根据医院设置id删除医院设置信息
    @ApiOperation(value = "根据医院设置id删除医院设置信息")
    @DeleteMapping(value = "/deleteById/{id}")
    public R deleteById(@PathVariable Integer id) {
        hospitalSetService.removeById(id);
        return R.ok();
    }


    // 分页方法
    @ApiOperation(value = "分页医院设置列表")
    @GetMapping("{page}/{limit}")
    public R pageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit
    ) {
        Page<HospitalSet> pageParam = new Page<>();

        hospitalSetService.page(pageParam, null);
        List<HospitalSet> records = pageParam.getRecords();
        long total = pageParam.getTotal();

        return R.ok().data("total", total).data("rows", records);
    }

}

