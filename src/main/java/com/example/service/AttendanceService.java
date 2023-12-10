package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Attendance;

public interface AttendanceService extends IService<Attendance> {
    IPage<Attendance> getAttendance(Page<Attendance> page, LambdaQueryWrapper<Attendance> attendanceLambdaQueryWrapper);
}
