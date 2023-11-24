package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.LeaveApplication;

public interface LeaveApplicationService extends IService<LeaveApplication> {
    IPage<LeaveApplication> getAllLeaveApplication(Page<LeaveApplication>page, LambdaQueryWrapper<LeaveApplication> leaveApplicationLambdaQueryWrapper);
}
