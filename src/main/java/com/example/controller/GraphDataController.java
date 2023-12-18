package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.common.Result;
import com.example.entity.Attendance;
import com.example.entity.requestDataForm.AttendanceGraph;
import com.example.entity.requestDataForm.EmployeeGraph;
import com.example.entity.role.Employee;
import com.example.service.AttendanceService;
import com.example.service.EmployeeService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.utils.Servicelogic.BaseInfoGet.getHttpServletRequestJwt;
import static com.example.utils.Servicelogic.DateProcess.getAllDatesInMonth;
import static com.example.utils.Servicelogic.DateProcess.setOneDayRange;

@RequestMapping("/graph")
@RestController
@Slf4j
public class GraphDataController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/attendance")
    public Result<Object> getAttendanceData(HttpServletRequest req, @RequestParam int year, @RequestParam int month){
        Claims claims = getHttpServletRequestJwt(req);
        if( (int)claims.get("authority") > 9 ){
            return Result.error("您没有权限");
        }

        List<Date> allMonthDays = getAllDatesInMonth(year, month);
        List<AttendanceGraph> dates = new ArrayList<>();
        for( int i = 0; i < allMonthDays.size(); i++){
            Date date = allMonthDays.get(i);
            LambdaQueryWrapper<Attendance> lambdaQueryWrapper = Wrappers.lambdaQuery(Attendance.class);
            setOneDayRange(lambdaQueryWrapper, date);
            long allCount = attendanceService.count(lambdaQueryWrapper);
            lambdaQueryWrapper.eq(Attendance::getStatus, 1);
            long attendanceCount = attendanceService.count(lambdaQueryWrapper);

            AttendanceGraph attendanceGraph = new AttendanceGraph();
            attendanceGraph.setDay(i+1);
            attendanceGraph.setTotal(allCount);
            attendanceGraph.setCheck(attendanceCount);
            dates.add(attendanceGraph);
        }
        return Result.success("查询成功", dates);
    }

    @GetMapping("/employeeInfoData")
    public Result<Object> getEmployeeInfoData(HttpServletRequest req){
        Claims claims = getHttpServletRequestJwt(req);
        if( (int)claims.get("authority") > 9 ){
            return Result.error("您没有权限");
        }
        EmployeeGraph employeeGraph = employeeService.getEmpGraphData();
        return Result.success("成功获取数据", employeeGraph);
    }
}
