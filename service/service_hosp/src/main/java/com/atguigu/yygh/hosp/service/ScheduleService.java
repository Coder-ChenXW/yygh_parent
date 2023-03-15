package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Schedule;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ScheduleService {

    void saveSchedule(Map<String, Object> stringObjectMap);

    Page<Schedule> getSchedulePage(Map<String, Object> stringObjectMap);

    void remove(Map<String, Object> stringObjectMap);

}
