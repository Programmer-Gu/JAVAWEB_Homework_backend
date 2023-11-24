package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.LeaveApplication;
import com.example.mapper.LeaveApplicationMapper;
import com.example.service.LeaveApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaveApplicationServiceImpl extends ServiceImpl<LeaveApplicationMapper, LeaveApplication> implements LeaveApplicationService {
    @Autowired
    private LeaveApplicationMapper leaveApplicationMapper;

    @Override
    public IPage<LeaveApplication> getAllLeaveApplication(Page<LeaveApplication> page, LambdaQueryWrapper<LeaveApplication> leaveApplicationLambdaQueryWrapper) {
        return leaveApplicationMapper.selectPage(page,leaveApplicationLambdaQueryWrapper);
    }
}
