package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Attendance;
import com.example.entity.role.Employee;

import java.util.Date;
import java.util.List;

public interface AttendanceService extends IService<Attendance> {
    IPage<Attendance> getAttendance(Page<Attendance> page, LambdaQueryWrapper<Attendance> attendanceLambdaQueryWrapper);

    List<Employee> getAttendanceEmployee(int state, Date OneDay);
}
