package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Attendance;
import com.example.mapper.AttendanceMapper;
import com.example.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements AttendanceService {
    @Autowired
    private AttendanceMapper attendanceMapper;

    public IPage<Attendance> getAttendance(Page<Attendance> page, LambdaQueryWrapper<Attendance> attendanceLambdaQueryWrapper){
        return attendanceMapper.selectPage(page, attendanceLambdaQueryWrapper);
    }
}
