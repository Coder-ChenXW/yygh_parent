package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.hosp.bean.ResultCodeEnum;
import com.atguigu.yygh.hosp.repository.ScheduleRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.BookingRule;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.BookingScheduleRuleVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public void saveSchedule(Map<String, Object> stringObjectMap) {

        Schedule schedule = JSONObject.parseObject(JSONObject.toJSONString(stringObjectMap), Schedule.class);

        String hoscode = schedule.getHoscode();
        String depcode = schedule.getDepcode();
        String hosScheduleId = schedule.getHosScheduleId();

        Schedule platformSchedule = scheduleRepository.findByHoscodeAndDepcodeAndHosScheduleId(hoscode, depcode, hosScheduleId);

        if (platformSchedule == null) {
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            scheduleRepository.save(schedule);
        } else {
            schedule.setCreateTime(platformSchedule.getCreateTime());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(platformSchedule.getIsDeleted());
            schedule.setId(platformSchedule.getId());
            scheduleRepository.save(schedule);
        }

    }

    @Override
    public Page<Schedule> getSchedulePage(Map<String, Object> stringObjectMap) {

        Schedule schedule = new Schedule();
        String hoscode = (String) stringObjectMap.get("hoscode");
        schedule.setHoscode(hoscode);

        Example<Schedule> scheduleExample = Example.of(schedule);

        int page = Integer.parseInt(stringObjectMap.get("page").toString());
        int limit = Integer.parseInt(stringObjectMap.get("limit").toString());

        PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by("createTime").ascending());

        Page<Schedule> result = scheduleRepository.findAll(scheduleExample, pageRequest);

        return result;
    }

    @Override
    public void remove(Map<String, Object> stringObjectMap) {
        String hoscode = (String) stringObjectMap.get("hoscode");
        String hoScheduleId = (String) stringObjectMap.get("hoScheduleId");

        Schedule schedule = scheduleRepository.findByHoscodeAndHosScheduleId(hoscode, hoScheduleId);

        if (schedule != null) {
            scheduleRepository.deleteById(schedule.getId());
        }

    }


    @Override
    public Map<String, Object> page(Integer pageNum, Integer pageSize, String hoscode, String depcode) {

        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
                        .first("workDate").as("workDate")
                        .count().as("count")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                Aggregation.sort(Sort.Direction.ASC, "workDate"),
                Aggregation.skip((pageNum - 1) * pageSize),
                Aggregation.limit(pageSize)
        ); // 聚合条件

        AggregationResults<BookingScheduleRuleVo> aggregate = mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);

        List<BookingScheduleRuleVo> mappedResults = aggregate.getMappedResults();
        for (BookingScheduleRuleVo bookingScheduleRuleVo : mappedResults) {
            Date workDate = bookingScheduleRuleVo.getWorkDate();
            String dayOfWeek = this.getDayOfWeek(new DateTime(workDate));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
        }

        Aggregation aggregation2 = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")); // 聚合条件

        AggregationResults<BookingScheduleRuleVo> aggregate2 = mongoTemplate.aggregate(aggregation2, Schedule.class, BookingScheduleRuleVo.class);

        Map<String, Object> map = new HashMap<>();
        map.put("list", mappedResults);
        map.put("total", aggregate2.getMappedResults().size());

        //获取医院名称
        Hospital hospital = hospitalService.getHospitalByHoscode(hoscode);
        //其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hosname", hospital.getHosname());
        map.put("baseMap", baseMap);

        return map;
    }

    @Override
    public List<Schedule> detail(String hoscode, String depcode, String workdate) {

        Date date = new DateTime(workdate).toDate();
        List<Schedule> scheduleList = scheduleRepository.findByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, date);

        scheduleList.stream().forEach(item -> {
            this.packageSchedule(item);
        });

        return scheduleList;
    }

    @Override
    public Map<String, Object> getSchedulePageByCondition(String hoscode, String depcode, Integer pageNum, Integer pageSize) {
        Hospital hospital = hospitalService.getHospitalByHoscode(hoscode);
        if (hospital == null) {
            throw new YyghException(20001, "医院不存在");
        }

        BookingRule bookingRule = hospital.getBookingRule();
        // 获取可预约日期分页数据
        IPage<Date> page = this.getListDate(pageNum, pageSize, bookingRule);

        List<Date> records = page.getRecords();

        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode).and("workDate").in(records);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate").first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                Aggregation.sort(Sort.Direction.ASC, "workDate")
        );

        AggregationResults<BookingScheduleRuleVo> aggregate = mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);

        List<BookingScheduleRuleVo> mappedResults = aggregate.getMappedResults();

        Map<Date, BookingScheduleRuleVo> collect = mappedResults.stream().collect(Collectors.toMap(BookingScheduleRuleVo::getWorkDate, BookingScheduleRuleVo -> BookingScheduleRuleVo));

        int size = records.size();

        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Date date = records.get(i);
            BookingScheduleRuleVo bookingScheduleRuleVo = collect.get(date);
            if (bookingScheduleRuleVo == null) {
                bookingScheduleRuleVo = new BookingScheduleRuleVo();
                bookingScheduleRuleVo.setWorkDate(date);
//                bookingScheduleRuleVo.setWorkDateMd(date);
//                bookingScheduleRuleVo.setDayOfWeek(this.getDayOfWeek(new DateTime(date)));
                bookingScheduleRuleVo.setDocCount(0);
                bookingScheduleRuleVo.setReservedNumber(0);
                bookingScheduleRuleVo.setAvailableNumber(0); // 当天所有医生总的剩余可预约数
                bookingScheduleRuleVo.setStatus(0);
            }



            bookingScheduleRuleVo.setWorkDateMd(date);
            bookingScheduleRuleVo.setDayOfWeek(this.getDayOfWeek(new DateTime(date)));
            bookingScheduleRuleVo.setStatus(0);

            // 第一页的第一条做特殊处理
            if (i == 0 && pageNum == 1) {
                DateTime dateTime = this.getDateTime(new Date(), bookingRule.getStopTime());
                // 如果医院规定的挂号时间在现在这个时间之前，说明已经过了当前的挂号时间
                if (dateTime.isBeforeNow()) {
                    bookingScheduleRuleVo.setStatus(-1);
                }
            }

            // 最后一页的最后一条做特殊处理
            if (pageNum == page.getPages() && i == size - 1) {
                bookingScheduleRuleVo.setStatus(1);
            }

            bookingScheduleRuleVoList.add(bookingScheduleRuleVo);

        }

        Map<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("list", bookingScheduleRuleVoList);

        Map<String, Object> baseMap = new HashMap<>();

        baseMap.put("hosname", hospitalService.getHospitalByHoscode(hoscode).getHosname());

        Department department = departmentService.getDepartment(hoscode, depcode);

        baseMap.put("bigname", department.getBigname());
        baseMap.put("depname", department.getDepname());

        baseMap.put("depname", departmentService.getDepName(hoscode, depcode));
        baseMap.put("workDateString", new DateTime().toString("yyyy年MM月"));
        baseMap.put("releaseTime", bookingRule.getReleaseTime());
        baseMap.put("stopTime", bookingRule.getStopTime());

        map.put("baseMap", baseMap);

        return map;
    }

    @Override
    public Schedule getScheduleInfo(String scheduleId) {

        Schedule schedule = scheduleRepository.findById(scheduleId).get();

        this.packageSchedule(schedule);

        return schedule;
    }

    private IPage getListDate(Integer pageNum, Integer pageSize, BookingRule bookingRule) {
        Integer cycle = bookingRule.getCycle();
        // 此时此刻是否已经超过了医院规定的当前挂号起始时间,如果超过了,则cycle+1
        String releaseTime = bookingRule.getReleaseTime();

        // 今天医院规定的预约挂号的起始时间
        DateTime dateTime = this.getDateTime(new Date(), releaseTime);
        if (dateTime.isBeforeNow()) {
            cycle = cycle + 1;
        }

        List<Date> list = new ArrayList<>();
        for (int i = 0; i < cycle; i++) {
            list.add(new DateTime(new DateTime().plusDays(i).toString("yyyy-MM-dd")).toDate());
        }

        // 计算页
        int start = (pageNum - 1) * pageSize;
        int end = start + pageSize;
        if (end > list.size()) {
            end = list.size();
        }

        List<Date> currentPageDateList = new ArrayList<>();

        for (int j = start; j < end; j++) {
            Date date = list.get(j);
            currentPageDateList.add(date);
        }


        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Date> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageSize, list.size());
        page.setRecords(currentPageDateList);


        return page;
    }

    // 将Date日期转换为DateTime日期
    private DateTime getDateTime(Date date, String timeString) {
        String dateTimeString = new DateTime(date).toString("yyyy-MM-dd") + " " + timeString;
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(dateTimeString);
        return dateTime;
    }


    private void packageSchedule(Schedule schedule) {
        //设置医院名称
        schedule.getParam().put("hosname", hospitalService.getHospitalByHoscode(schedule.getHoscode()).getHosname());
        //设置科室名称
        schedule.getParam().put("depname", departmentService.getDepName(schedule.getHoscode(), schedule.getDepcode()));
        //设置日期对应星期
        schedule.getParam().put("dayOfWeek", this.getDayOfWeek(new DateTime(schedule.getWorkDate())));
    }


    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }

}
