package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.common.Result;
import com.example.entity.Attendance;
import com.example.service.AttendanceService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.example.utils.Servicelogic.BaseInfoGet.getHttpServletRequestJwt;
import static com.example.utils.Servicelogic.DateProcess.getOneDayDate;
import static com.example.utils.Servicelogic.DateProcess.setOneDayRange;

@RequestMapping("/employee")
@RestController
@Slf4j
public class EmployeeAttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/toAttendance")
    public Result<Object> toAttendance(HttpServletRequest req){
        Claims claims = getHttpServletRequestJwt(req);
        if( claims.get("employeeId") == null ){
            return Result.error("您不是教职工");
        }

        LambdaQueryWrapper<Attendance> lambdaQueryWrapper = Wrappers.lambdaQuery(Attendance.class);
        lambdaQueryWrapper.eq(Attendance::getEmployeeId, claims.get("employeeId"));
        lambdaQueryWrapper.eq(Attendance::getStatus, 1);
        setOneDayRange(lambdaQueryWrapper, null);
        if( attendanceService.count(lambdaQueryWrapper) > 0 ){
            return Result.error("今日已打卡!");
        }

        LambdaUpdateWrapper<Attendance> attendanceLambdaUpdateWrapper = Wrappers.lambdaUpdate(Attendance.class);
        attendanceLambdaUpdateWrapper.set(Attendance::getStatus, 1);
        attendanceLambdaUpdateWrapper.eq(Attendance::getStatus, 0);
        attendanceLambdaUpdateWrapper.eq(Attendance::getEmployeeId, claims.get("employeeId"));
        setOneDayRange(attendanceLambdaUpdateWrapper, null);
        boolean ifSuccess = attendanceService.update(attendanceLambdaUpdateWrapper);

        if( ifSuccess ){
            return Result.success("打卡成功！", null);
        }
        return Result.error("打卡失败！");
    }

    @GetMapping("/getOneDayAttendance")
    public Result<Object> getLastWeekAttendance(HttpServletRequest req, @RequestParam int year, @RequestParam int month, @RequestParam int day){
        Claims claims = getHttpServletRequestJwt(req);
        if( (int)claims.get("authority") > 9 ){
            return Result.error("您没有权限");
        }

        Date datetime = getOneDayDate(year,month,day);
        LambdaQueryWrapper<Attendance> lambdaQueryWrapper = Wrappers.lambdaQuery(Attendance.class);
        lambdaQueryWrapper.select(Attendance::getRealName, Attendance::getStatus);
        setOneDayRange(lambdaQueryWrapper, datetime);
        List<Attendance> resInfo = attendanceService.list(lambdaQueryWrapper);

        if( resInfo != null ){
            return Result.success("查询成功", resInfo);
        }
        return Result.error("查询失败");
    }

    @GetMapping("/getTodayAttendanceNumber")
    public Result<Object> getTodayAttendanceNumber(HttpServletRequest req){
        Claims claims = getHttpServletRequestJwt(req);
        if( (int)claims.get("authority") > 9 ){
            return Result.error("您没有权限");
        }

        LambdaQueryWrapper<Attendance> lambdaQueryWrapper = Wrappers.lambdaQuery(Attendance.class);
        setOneDayRange(lambdaQueryWrapper, null);
        long todayCount = attendanceService.count(lambdaQueryWrapper);

        return Result.success("查询成功", null).add("todayAttendance", todayCount);
    }
}
