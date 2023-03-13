package com.atguigu.yygh.hosp.controller;


import com.atguigu.yygh.hosp.bean.Result;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.utils.HttpRequestHelper;
import com.atguigu.yygh.model.hosp.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@RequestMapping("/api/hosp")
public class DepartmentController {


    @Autowired
    private DepartmentService departmentService;


    /**
     * @description: 查询科室信息
     * @author: ChenXW
     * @date: 2023-03-13 23:39
     */
    @PostMapping("/department/list")
    public Result<Page> getDepartmentPage(HttpServletRequest httpServletRequest) {
        Map<String, Object> stringObjectMap = HttpRequestHelper.switchMap(httpServletRequest.getParameterMap());

        Page<Department> page = departmentService.getDepartmentPage(stringObjectMap);

        return Result.ok(page);
    }


    /**
     * @description: 科室保存
     * @author: ChenXW
     * @date: 2023-03-13 23:15
     */
    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        Map<String, Object> stringObjectMap = HttpRequestHelper.switchMap(request.getParameterMap());
        // 验证
        departmentService.saveDepartment(stringObjectMap);

        return Result.ok();
    }

}
