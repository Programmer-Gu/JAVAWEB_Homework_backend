package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.LeaveApplication;
import com.example.service.LeaveApplicationService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.utils.Servicelogic.BaseInfoGet.getHttpServletRequestJwt;

@RequestMapping("/employee")
@RestController
@Slf4j
public class EmployeeLeaveController {
    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @GetMapping("/getAllLeaveApplication/{pageNumber}")
    public Result<Object> getAllLeaveApplication(HttpServletRequest req, @PathVariable int pageNumber) {
        Claims claims = getHttpServletRequestJwt(req);
        if ((int) claims.get("authority") > 2) {
            return Result.error("对不起，您的权限不足！");
        }

        LambdaQueryWrapper<LeaveApplication> lambdaQueryWrapper = Wrappers.lambdaQuery(LeaveApplication.class);
        lambdaQueryWrapper.eq(LeaveApplication::getStatus, 0);
        Page<LeaveApplication> page = new Page<>(pageNumber, 10);
        IPage<LeaveApplication> resInfo = leaveApplicationService.getAllLeaveApplication(page, lambdaQueryWrapper);

        if (resInfo != null) {
            return Result.success("查询成功", resInfo);
        }
        return Result.error("查询失败");
    }

    @PostMapping("/leaveApplicationProcess/{ifPass}")
    public Result<Object> leaveApplicationSubmit(HttpServletRequest req, @RequestParam int applicationId, @PathVariable boolean ifPass) {
        Claims claims = getHttpServletRequestJwt(req);
        if ((int) claims.get("authority") > 2) {
            return Result.error("对不起，您的权限不足！");
        }

        LambdaUpdateWrapper<LeaveApplication> leaveApplicationLambdaUpdateWrapper = Wrappers.lambdaUpdate(LeaveApplication.class);
        leaveApplicationLambdaUpdateWrapper.eq(LeaveApplication::getApplicationId, applicationId);
        leaveApplicationLambdaUpdateWrapper.set(LeaveApplication::getStatus, (ifPass ? 1 : -1));
        boolean ifSuccess = leaveApplicationService.update(leaveApplicationLambdaUpdateWrapper);

        if (ifSuccess) {
            return Result.success("修改成功", null);
        }
        return Result.error("修改失败");
    }

    @PostMapping("/leaveApplication")
    public Result<Object> leaveApplication(HttpServletRequest req, @RequestBody LeaveApplication leaveApplication) {
        Claims claims = getHttpServletRequestJwt(req);
        if ((int) claims.get("authority") > 9) {
            return Result.error("对不起，您不是教职工！");
        }

        LambdaQueryWrapper<LeaveApplication> lambdaQueryWrapper = Wrappers.lambdaQuery(LeaveApplication.class);
        lambdaQueryWrapper.eq(LeaveApplication::getEmployeeId, claims.get("employeeId"));
        lambdaQueryWrapper.eq(LeaveApplication::getStartDate, leaveApplication.getStartDate());
        lambdaQueryWrapper.eq(LeaveApplication::getEndDate, leaveApplication.getEndDate());

        leaveApplication.setEmployeeId((int) claims.get("employeeId"));
        boolean ifSuccess = leaveApplicationService.save(leaveApplication);

        if (ifSuccess) {
            return Result.success("发送成功", null);
        }
        return Result.error("发送失败");
    }

    @GetMapping("/getMyLeaveApplication")
    public Result<Object> getMyLeaveApplication(HttpServletRequest req) {
        Claims claims = getHttpServletRequestJwt(req);
        if ((int) claims.get("authority") > 9) {
            return Result.error("对不起，您不是教职工！");
        }

        LambdaQueryWrapper<LeaveApplication> leaveApplicationLambdaQueryWrapper = Wrappers.lambdaQuery(LeaveApplication.class);
        leaveApplicationLambdaQueryWrapper.eq(LeaveApplication::getEmployeeId, claims.get("employeeId"));
        List<LeaveApplication> leaveApplications = leaveApplicationService.list(leaveApplicationLambdaQueryWrapper);

        if (leaveApplications != null) {
            return Result.success("查询成功", leaveApplications);
        }
        return Result.error("查询失败");
    }
}
