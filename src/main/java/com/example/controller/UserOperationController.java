package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.common.Result;
import com.example.entity.role.Employee;
import com.example.entity.role.User;
import com.example.service.EmployeeService;
import com.example.service.UserService;
import com.example.utils.Servicelogic.GenerateWrapper;
import com.example.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserOperationController {
    @Autowired
    private UserService userService;
    @Autowired
    private EmployeeService employeeService;

    /**
     * 1.用户登录操作
     *
     * @param user 用户
     * @return jwt
     */
    @PostMapping("/login")
    public Result<Object> login(@RequestBody User user) {
        String password = user.getPassword();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, user.getUserName());
        User tmp_user = userService.getOne(queryWrapper);
        //3.如果没有查询到则返回失败结果
        if (tmp_user == null) {
            return Result.error("登录失败，没有查询到账户信息").setCode(400);
        }
        //4.根据用户id查询，进行密码比对，如果不一致则返回登录失败结果
        String right_password = tmp_user.getPassword();
        if (!right_password.equals(password)) {
            return Result.error("登录失败，密码输入错误").setCode(400);
        }

        //6.登录成功，将用户id存入Session并返回登陆成功的结果
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", tmp_user.getUserId());
        claims.put("authority", tmp_user.getAuthority());
        claims.put("userName", tmp_user.getUserName());
        claims.put("gender", tmp_user.getGender());
        claims.put("brithDate", tmp_user.getBirthDate());
        claims.put("idNumber", tmp_user.getIdNumber());

        //6.5 登录成功，查询表内的教职工信息
        LambdaQueryWrapper<Employee> employeeQueryWrapper = new LambdaQueryWrapper<>();
        employeeQueryWrapper.eq(Employee::getUserId, tmp_user.getUserId());
        Employee employee = employeeService.getOne(employeeQueryWrapper);
        if (employee != null) {
            log.info(String.format("用户%s为教师", tmp_user.getUserName()));
            claims.put("employeeId", employee.getEmployeeId());
            claims.put("departmentId", employee.getDepartmentId());
            claims.put("positionsId", employee.getPositionsId());
            claims.put("academicTitleId", employee.getAcademicTitleId());
        }

        String jwt = JwtUtils.generateJwt(claims);//jwt包含了当前登录的员工信息

        //返回结果
        return Result.success(String.format("用户{%s}登录成功", user.getUserId()), null)
                .setCode(200)
                .add("jwt", jwt)
                .setMsg("登录成功");
    }

    @GetMapping("/info")
    public Result<Object> getJwtInfo(HttpServletRequest req) {
        String jwt = req.getHeader("Token");
        Claims claims = JwtUtils.parseJWT(jwt);

        HashMap<String, Object> userData = new HashMap<>();
        userData.put("userId", claims.get("userId"));
        userData.put("authority", claims.get("authority"));
        userData.put("userName", claims.get("userName"));
        userData.put("gender", claims.get("gender"));
        userData.put("brithDate", claims.get("brithDate"));
        userData.put("idNumber", claims.get("idNumber"));

        return Result.success("成功返回用户信息！", userData);
    }

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return result
     */
    @PostMapping("/register")
    public Result<Object> register(HttpServletRequest req, @RequestBody User user) {
        if( user.getUserName() == null || user.getPassword() == null){
            return Result.error("注册信息缺失！");
        }
        if (userNameCheck(user.getUserName())) {
            return Result.error("该名称已在本系统注册！").setCode(403);
        }

        //5.为用户设置初始值
        User userData = new User();
        userData.setUserName(user.getUserName());
        userData.setPassword(user.getPassword());
        userData.setAuthority(10);
        //6.在数据库中保存用户
        try {
            userService.save(userData);
            log.info("新用户插入成功");
            return Result.success("注册成功", null).setCode(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("用户注册失败，请联系管理员").setCode(503);
    }

    /**
     * 用户修改个人信息
     *
     * @param user 用户信息
     * @return result
     */
    @PatchMapping("/updateInfo")
    public Result<Object> updateUserInfo(HttpServletRequest req, @RequestBody User user) {
        String jwt = req.getHeader("Token");
        Claims claims = JwtUtils.parseJWT(jwt);

        if(!claims.get("userName").equals(user.getUserName()) && userNameCheck(user.getUserName())) {
            Result.error("用户的昵称已存在！");
        }

        UpdateWrapper<User> updateWrapper = GenerateWrapper.getUpdateWrapper(user, "user_id", "authority");
        updateWrapper.eq("user_id", claims.get("userId"));
        boolean ifSuccess = userService.update(updateWrapper);

        return (ifSuccess ? Result.success("用户信息修改成功", null) : Result.error("用户信息修改失败"));
    }

    private boolean userNameCheck(String userName) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserName, userName);
        return userService.count(userLambdaQueryWrapper) > 0;
    }
}

