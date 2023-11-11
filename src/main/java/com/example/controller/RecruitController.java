package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.Result;
import com.example.entity.role.Recruit;
import com.example.service.RecruitService;
import com.example.utils.EmptyDataCheck;
import com.example.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recruit")
@Slf4j
public class RecruitController {
    @Autowired
    private RecruitService recruitService;

    @PostMapping("/sendRecruit")
    public Result<Object> sendRecruit(HttpServletRequest req, @RequestBody Recruit recruit) {
        if(!EmptyDataCheck.checkData(recruit, "email","name","content")){
            return Result.error("数据缺失！");
        }

        Claims claims = getHttpServletRequestJwt(req);
        recruit.setState(0);
        recruit.setAskerId((int) claims.get("userId"));

        LambdaQueryWrapper<Recruit>lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Recruit::getAskerId, recruit.getAskerId());
        lambdaQueryWrapper.ge(Recruit::getState, 0);
        if( recruitService.count(lambdaQueryWrapper) > 0){
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
        if( recruit == null ){
            return Result.error("对不起，您没有在投的简历！");
        }
        int ifSuccess = recruitService.closeRecruit(recruit.getRecruitId());

        if (ifSuccess > 0) {
            return Result.success("简历关闭成功！", null);
        }
        return Result.error("简历关闭失败");
    }

    @GetMapping("/getAllRecruit")
    public Result<Object> getAllRecruit(HttpServletRequest req) {
        Claims claims = getHttpServletRequestJwt(req);
        if ((int) claims.get("authority") > 1) {
            return Result.error("对不起，您的权限不足！");
        }

        List<Recruit> res = recruitService.getRecruitList();
        if (res != null) {
            return Result.success("查询所有简历成功！", res);
        }
        return Result.error("简历查询失败");
    }

    private Claims getHttpServletRequestJwt(HttpServletRequest req){
        String jwt = req.getHeader("Token");
        return JwtUtils.parseJWT(jwt);
    }


}
