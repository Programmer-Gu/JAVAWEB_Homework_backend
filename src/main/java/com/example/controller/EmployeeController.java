package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.AcademicTitle;
import com.example.entity.Department;
import com.example.entity.LeaveApplication;
import com.example.entity.Positions;
import com.example.entity.role.Employee;
import com.example.entity.selectEntity.DetailedEmployeeInfo;
import com.example.service.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.example.utils.Servicelogic.BaseInfoGet.getHttpServletRequestJwt;
import static com.example.utils.Servicelogic.GenerateWrapper.getUpdateWrapper;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private LeaveApplicationService leaveApplicationService;
    @Autowired
    private PositionsService positionsService;
    @Autowired
    private AcademicTitleService academicTitleService;
    @Autowired
    private DepartmentService departmentService;

    /**
     * @param req http请求参数
     * @return
     */
    @GetMapping("/getMyEmployeeInfo")
    public Result<Object> getUserEmployeeInfo(HttpServletRequest req) {
        Claims claims = getHttpServletRequestJwt(req);

        if (claims.get("employeeId") != null) {
            int employeeId = (int) claims.get("employeeId");
            Employee employee = employeeService.getById(employeeId);
            Positions positions = positionsService.getById(employee.getPositionsId());
            Department department = departmentService.getById(employee.getDepartmentId());
            AcademicTitle academicTitle = academicTitleService.getById(employee.getAcademicTitleId());

            DetailedEmployeeInfo detailedEmployeeInfo = new DetailedEmployeeInfo();
            detailedEmployeeInfo.setEmployee(employee);
            detailedEmployeeInfo.setDepartment(department);
            detailedEmployeeInfo.setPositions(positions);
            detailedEmployeeInfo.setAcademicTitle(academicTitle);

            return Result.success("就职信息查询成功！", detailedEmployeeInfo);
        }
        return Result.error("您不是本校职工！");
    }

    @GetMapping("/getAllInfo")
    public Result<Object> getAllDepartmentList(HttpServletRequest req, @RequestParam int infoId) {
        Claims claims = getHttpServletRequestJwt(req);
        if ((int) claims.get("authority") > 9) {
            return Result.error("对不起，您的权限不足！");
        }

        List<?> resInfo = selectInfo(infoId);

        if (resInfo != null) {
            return Result.success("查询成功", resInfo);
        }
        return Result.error("查询失败");
    }


    @PatchMapping("/updateEmployee")
    public Result<Object> updateEmployeeInfo(HttpServletRequest req, @RequestBody Employee employee) {
        Claims claims = getHttpServletRequestJwt(req);
        if ((int) claims.get("authority") > 2) {
            return Result.error("对不起，您的权限不足！");
        }

        UpdateWrapper<Employee> updateWrapper = getUpdateWrapper(employee, "employee_id", "join_date", "user_id");
        updateWrapper.eq("employee_id", claims.get("employeeId"));
        boolean ifSuccess = employeeService.update(updateWrapper);

        if (ifSuccess) {
            return Result.success("修改成功", null);
        }
        return Result.error("修改失败");
    }

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

    @PostMapping("/leaveApplicationProcess/{applicationId}/{ifPass}")
    public Result<Object> leaveApplicationSubmit(HttpServletRequest req, @PathVariable int applicationId, @PathVariable boolean ifPass) {
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
        long test = leaveApplicationService.count(lambdaQueryWrapper);
        if (leaveApplicationService.count(lambdaQueryWrapper) > 0) {
            return Result.error("您还有未批准的假期请求！");
        }

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

    private List<?> selectInfo(int infoId) {
        int dateClass = 3;
        if (infoId >= dateClass) {
            return null;
        }

        if (infoId == 0) {
            LambdaQueryWrapper<Positions> positionsLambdaQueryWrapper = Wrappers.lambdaQuery(Positions.class);
            positionsLambdaQueryWrapper.select(Positions::getPositionsId, Positions::getPositionsName, Positions::getDescription, Positions::getSalary);
            return positionsService.list(positionsLambdaQueryWrapper);
        } else if (infoId == 1) {
            LambdaQueryWrapper<AcademicTitle> academicTitleLambdaQueryWrapper = Wrappers.lambdaQuery(AcademicTitle.class);
            academicTitleLambdaQueryWrapper.select(AcademicTitle::getAcademicTitleId);
            academicTitleLambdaQueryWrapper.select(AcademicTitle::getTitleName);
            return academicTitleService.list(academicTitleLambdaQueryWrapper);
        } else if (infoId == 2) {
            LambdaQueryWrapper<Department> departmentLambdaQueryWrapper = Wrappers.lambdaQuery(Department.class);
            departmentLambdaQueryWrapper.select(Department::getDepartmentId);
            departmentLambdaQueryWrapper.select(Department::getDepartmentName);
            return departmentService.list(departmentLambdaQueryWrapper);
        }
        return null;
    }
}
