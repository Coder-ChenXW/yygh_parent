package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Department;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface DepartmentService  {

    void saveDepartment(Map<String, Object> stringObjectMap);

    Page<Department> getDepartmentPage(Map<String, Object> stringObjectMap);

}
