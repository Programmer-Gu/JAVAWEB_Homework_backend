package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.role.Employee;
import com.example.entity.role.Recruit;
import com.example.entity.role.User;
import com.example.service.EmailService;
import com.example.service.EmployeeService;
import com.example.service.RecruitService;
import com.example.service.UserService;
import com.example.utils.Servicelogic.EmptyDataCheck;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.utils.Servicelogic.BaseInfoGet.getHttpServletRequestJwt;

@RestController
@RequestMapping("/recruit")
@Slf4j
public class RecruitController {
    @Autowired
    private UserService userService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private RecruitService recruitService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/sendRecruit")
    public Result<Object> sendRecruit(HttpServletRequest req, @RequestBody Recruit recruit) {
        Claims claims = getHttpServletRequestJwt(req);
        if (claims.get("employee_id") != null) {
            return Result.error("你已经入职了，滚出去！");
        }

        if (!EmptyDataCheck.checkData(recruit, "email", "name", "content")) {
            return Result.error("数据缺失！");
        }

        recruit.setState(0);
        recruit.setAskerId((int) claims.get("userId"));

        LambdaQueryWrapper<Recruit> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Recruit::getAskerId, recruit.getAskerId());
        lambdaQueryWrapper.ge(Recruit::getState, 0);
        if (recruitService.count(lambdaQueryWrapper) > 0) {
            return Result.error("对不起，您现在还有再投的简历！");
        }

        boolean ifSuccess = recruitService.save(recruit);
        if (ifSuccess) {
            return Result.success("简历发送成功！", null);
        }
        return Result.error("简历发送失败");
    }

    @GetMapping("/getRecruit")
    public Result<Object> getRecruit(HttpServletRequest req) {
        Claims claims = getHttpServletRequestJwt(req);

        Recruit recruit = recruitService.getRecruit((int) claims.get("userId"));
        if (recruit != null) {
            return Result.success("查询简历成功！", recruit);
        }
        return Result.success("没有查询到用户简历信息", new Recruit());
    }

    @DeleteMapping("/closeRecruit")
    public Result<Object> closeRecruit(HttpServletRequest req) {
        Claims claims = getHttpServletRequestJwt(req);
        Recruit recruit = recruitService.getRecruit((int) claims.get("userId"));
        if (recruit == null) {
            return Result.error("对不起，您没有在投的简历！");
        }
        int ifSuccess = recruitService.closeRecruit(recruit.getRecruitId());

        if (ifSuccess > 0) {
            return Result.success("简历关闭成功！", null);
        }
        return Result.error("简历关闭失败");
    }

    @GetMapping("/getAllRecruit/{state}/{pageNumber}")
    public Result<Object> getAllRecruit(HttpServletRequest req,@PathVariable int state, @PathVariable int pageNumber) {
        Claims claims = getHttpServletRequestJwt(req);
        if ((int) claims.get("authority") > 1) {
            return Result.error("对不起，您的权限不足！");
        }
        if( state < 0 || state > 3){
            return Result.error("非法路由");
        }

        Page<Recruit> page = new Page<>(pageNumber, 10);
        LambdaQueryWrapper<Recruit> queryWrapper = Wrappers.lambdaQuery(Recruit.class);
        queryWrapper.eq(Recruit::getState, state);
        IPage<Recruit> res = recruitService.getRecruitList(page, queryWrapper);
        if (res != null) {
            return Result.success("查询所有简历成功！", res);
        }
        return Result.error("简历查询失败");
    }

    @PostMapping("/passRecruitState/{ifPass}")
    @Transactional
    public Result<Object> passRecruitState(HttpServletRequest req, @PathVariable boolean ifPass, @RequestBody Recruit userRecruit) {
        Claims claims = getHttpServletRequestJwt(req);
        if ((int) claims.get("authority") > 2) {
            return Result.error("对不起，您的权限不足！");
        }

        String sqlMessage;
        if (!ifPass) {
            sqlMessage = "state = -1";
        } else {
            sqlMessage = "state = state + 1";
        }

        LambdaUpdateWrapper<Recruit> recruitLambdaUpdateWrapper = Wrappers.lambdaUpdate(Recruit.class);
        recruitLambdaUpdateWrapper.eq(Recruit::getRecruitId, userRecruit.getRecruitId()) // 假设你是根据id来定位记录
                .setSql(sqlMessage); // "your_field"是你想要增加的字段
        boolean ifSuccess = recruitService.update(recruitLambdaUpdateWrapper);

        if (ifSuccess) {
            LambdaQueryWrapper<Recruit> recruitLambdaQueryWrapper = Wrappers.lambdaQuery(Recruit.class);
            recruitLambdaQueryWrapper.select(Recruit::getState, Recruit::getAskerId);
            recruitLambdaQueryWrapper.eq(Recruit::getRecruitId, userRecruit.getRecruitId());
            Recruit recruit = recruitService.getOne(recruitLambdaQueryWrapper);

            LambdaUpdateWrapper<User> userLambdaUpdateWrapper = Wrappers.lambdaUpdate(User.class);
            userLambdaUpdateWrapper.eq(User::getUserId, recruit.getAskerId()).setSql("authority = 9");

            if (recruit.getState() >= 4) {
                Employee employee = new Employee();
                employee.setUserId(recruit.getAskerId());
                employeeService.save(employee);
                userService.update(userLambdaUpdateWrapper);
                return Result.success("用户成功入职", null);
            }

            if( userRecruit.getState() == 0 && userRecruit.getEmail() != null && userRecruit.getContent() != null ){
                emailService.sendSimpleMail(recruit.getEmail(),"招聘回复" ,userRecruit.getContent());
            }else{
                return Result.error("用户招聘信息缺失！");
            }
            return Result.success("用户该阶段通过！", null);
        }
        return Result.error("操作失败");
    }
}
