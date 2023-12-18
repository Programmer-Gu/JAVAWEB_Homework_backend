package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.common.Result;
import com.example.entity.AcademicTitle;
import com.example.entity.Department;
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
    private PositionsService positionsService;
    @Autowired
    private AcademicTitleService academicTitleService;
    @Autowired
    private DepartmentService departmentService;

    /**
     * @param req http请求参数
     * @return 获取职工个人信息
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
            detailedEmployeeInfo.setRealName(employee.getRealName());
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

    @GetMapping("/getSalary")
    public Result<Object> getSalary(HttpServletRequest req){
        Claims claims = getHttpServletRequestJwt(req);
        if ((int) claims.get("authority") > 9) {
            return Result.error("对不起，您的权限不足！");
        }

        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper =Wrappers.lambdaQuery(Employee.class);
        employeeLambdaQueryWrapper.ne(Employee::getRetired, 1);
        employeeLambdaQueryWrapper.select(Employee::getEmployeeId, Employee::getRealName, Employee::getSalary);
        List<Employee> getSalaryEmp = employeeService.list(employeeLambdaQueryWrapper);

        if ( getSalaryEmp != null ){
            return Result.success("获取成功", getSalaryEmp);
        }
        return Result.error("获取失败");
    }

    @PostMapping("/updateSalary")
    public Result<Object> updateSalary(HttpServletRequest req, @RequestBody Employee employee){
        Claims claims = getHttpServletRequestJwt(req);
        if ((int) claims.get("authority") > 9) {
            return Result.error("对不起，您的权限不足！");
        }

        LambdaUpdateWrapper<Employee> employeeLambdaUpdateWrapper = Wrappers.lambdaUpdate(Employee.class);
        employeeLambdaUpdateWrapper.eq(Employee::getEmployeeId, employee.getEmployeeId());
        employeeLambdaUpdateWrapper.set(Employee::getSalary, employee.getSalary());
        boolean ifSuccess = employeeService.update(employeeLambdaUpdateWrapper);

        if( ifSuccess ){
            return Result.success("修改成功", null);
        }
        return Result.error("修改失败");
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

    @GetMapping("/retiredList")
    public Result<Object> retireList(HttpServletRequest req){
        Claims claims = getHttpServletRequestJwt(req);
        if ((int) claims.get("authority") > 2) {
            return Result.error("对不起，您的权限不足！");
        }

        List<Employee> retiredList = employeeService.retiredList();

        if( retiredList != null ){
            return Result.success("退休人获取成功！", retiredList);
        }
        return Result.error("获取失败");
    }

    @PostMapping("/toRetired")
    public Result<Object> toRetired(HttpServletRequest req, @RequestBody Employee employee){
        Claims claims = getHttpServletRequestJwt(req);
        if ((int) claims.get("authority") > 2) {
            return Result.error("对不起，您的权限不足！");
        }

        LambdaUpdateWrapper<Employee> employeeLambdaUpdateWrapper = Wrappers.lambdaUpdate(Employee.class);
        employeeLambdaUpdateWrapper.eq(Employee::getEmployeeId, employee.getEmployeeId());
        employeeLambdaUpdateWrapper.set(Employee::getRetired, 1);
        boolean ifSuccess = employeeService.update(employeeLambdaUpdateWrapper);
        if( ifSuccess ){
            return Result.success(String.format("用户ID:%d，已自愿退休！", employee.getEmployeeId()), null);
        }
        return Result.error("退休失败！");
    }

    private List<?> selectInfo(int infoId) {
        int dateClass = 3;
        if (infoId >= dateClass) {
            return null;
        }

        if (infoId == 0) {
            LambdaQueryWrapper<Positions> positionsLambdaQueryWrapper = Wrappers.lambdaQuery(Positions.class);
            positionsLambdaQueryWrapper.select(Positions::getPositionsId, Positions::getPositionsName, Positions::getDescription);
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
