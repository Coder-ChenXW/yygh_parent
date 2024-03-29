package com.atguigu.yygh.hosp.controller.user;


import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user/hosp/schedule")
public class UserScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * @description: 方法复用
     * @author: ChenXW
     * @date: 2023-03-27 22:50
     */
    @GetMapping("/{scheduleId}")
    public ScheduleOrderVo getScheduleById(@PathVariable(value = "scheduleId") String scheduleId) {

        return scheduleService.getScheduleById(scheduleId);
    }


    /**
     * @description: 根据排班id获取排班信息
     * @author: ChenXW
     * @date: 2023-03-27 14:57
     */
    @GetMapping("/info/{scheduleId}")
    public R getScheduleInfo(@PathVariable String scheduleId) {

        Schedule schedule = scheduleService.getScheduleInfo(scheduleId);
        return R.ok().data("schedule", schedule);


    }

    /**
     * @description: 根据医院编号和科室编号获取排班规则数据 用于预约挂号页面的排班规则显示
     * @author: ChenXW
     * @date: 2023-03-27 10:24
     */
    @GetMapping("/{hoscode}/{depcode}/{pageNum}/{pageSize}")
    public R getSchedulePage(@PathVariable String hoscode,
                             @PathVariable String depcode,
                             @PathVariable Integer pageNum,
                             @PathVariable Integer pageSize) {

        Map<String, Object> map = scheduleService.getSchedulePageByCondition(hoscode, depcode, pageNum, pageSize);

        return R.ok().data(map);
    }

    /**
     * @description: 查询排班详情信息
     * @author: ChenXW
     * @date: 2023-03-27 13:45
     */
    @GetMapping("/{hoscode}/{depcode}/{workdDate}")
    public R getScheduleDetail(@PathVariable String hoscode,
                               @PathVariable String depcode,
                               @PathVariable String workdDate) {
        List<Schedule> details = scheduleService.detail(hoscode, depcode, workdDate);

        return R.ok().data("details", details);

    }

}
