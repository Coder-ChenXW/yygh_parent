package com.atguigu.yygh.hosp.controller.api;


import com.atguigu.yygh.hosp.bean.Result;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.hosp.utils.HttpRequestHelper;
import com.atguigu.yygh.model.hosp.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
public class ApiScheduleController {

    @Autowired
    private ScheduleService scheduleService;


    /**
     * @description: 排班列表删除
     * @author: ChenXW
     * @date: 2023-03-14 20:25
     */
    @PostMapping("/schedule/remove")
    public Result remove(HttpServletRequest request) {
        Map<String, Object> stringObjectMap = HttpRequestHelper.switchMap(request.getParameterMap());

        scheduleService.remove(stringObjectMap);

        return Result.ok();
    }


    /**
     * @description: 查询排班分页
     * @author: ChenXW
     * @date: 2023-03-14 10:41
     */
    @PostMapping("/schedule/list")
    public Result<Page> getSchedulePage(HttpServletRequest request) {
        Map<String, Object> stringObjectMap = HttpRequestHelper.switchMap(request.getParameterMap());

        Page<Schedule> schedulePage = scheduleService.getSchedulePage(stringObjectMap);

        return Result.ok(schedulePage);

    }


    /**
     * @description: 保存排班信息
     * @author: ChenXW
     * @date: 2023-03-14 09:53
     */
    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {

        Map<String, Object> stringObjectMap = HttpRequestHelper.switchMap(request.getParameterMap());

        scheduleService.saveSchedule(stringObjectMap);

        return Result.ok();
    }

}
