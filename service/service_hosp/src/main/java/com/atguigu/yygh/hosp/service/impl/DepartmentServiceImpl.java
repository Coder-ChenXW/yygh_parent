package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.DepartmentRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.model.hosp.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void saveDepartment(Map<String, Object> stringObjectMap) {

        Department department = JSONObject.parseObject(JSONObject.toJSONString(stringObjectMap), Department.class);

        String hoscode = department.getHoscode();
        String depcode = department.getDepcode();

        Department platformDepartment = departmentRepository.findByHoscodeAndDepcode(hoscode, depcode);

        if (platformDepartment == null) {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);

            departmentRepository.save(department);
        } else {
            department.setCreateTime(platformDepartment.getCreateTime());
            department.setUpdateTime(new Date());
            department.setIsDeleted(platformDepartment.getIsDeleted());
            department.setId(platformDepartment.getId());
            departmentRepository.save(department);
        }

    }

    @Override
    public Page<Department> getDepartmentPage(Map<String, Object> stringObjectMap) {
        Integer page = Integer.parseInt((String) stringObjectMap.get("page"));
        Integer limit = Integer.parseInt((String) stringObjectMap.get("limit"));

        String hoscode = (String) stringObjectMap.get("hoscode");

        Department department = new Department();
        department.setHoscode(hoscode);
        Example<Department> example = Example.of(department);

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Department> all = departmentRepository.findAll(example, pageable);

        return all;
    }

    @Override
    public void remove(Map<String, Object> stringObjectMap) {
        String hoscode = (String) stringObjectMap.get("hoscode");
        String depcode = (String) stringObjectMap.get("depcode");

        Department department = departmentRepository.findByHoscodeAndDepcode(hoscode, depcode);

        if (department != null) {
            departmentRepository.deleteById(department.getId());
        }
    }

}
