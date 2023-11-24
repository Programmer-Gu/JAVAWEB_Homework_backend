package com.example.controller;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Consult;
import com.example.entity.selectEntity.ConsultData;
import com.example.service.ConsultTableService;
import com.example.service.EmailService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.example.utils.Servicelogic.BaseInfoGet.getAuthority;
import static com.example.utils.Servicelogic.BaseInfoGet.getHttpServletRequestJwt;

@RestController
@RequestMapping("/consult")
@Slf4j
public class ConsultTableController {
    @Autowired
    private ConsultTableService consultTableService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/sendConsult")
    public Result<Object> sendConsult(HttpServletRequest req, @RequestBody Consult consult){
        Claims claims = getHttpServletRequestJwt(req);
        consult.setAskerId((int)claims.get("userId"));
        boolean ifSuccess = consultTableService.save(consult);

        if( ifSuccess ){
            return Result.success("咨询发送成功！", null);
        }
        return Result.error("咨询发送失败");
    }

    @GetMapping("/getAllConsult/{pageNumber}")
    public Result<Object> getConsult(HttpServletRequest req, @PathVariable int pageNumber){
        Claims claims = getHttpServletRequestJwt(req);
        if (getAuthority(claims,2)) {
            return Result.error("对不起，您的权限不足！");
        }

        Page<ConsultData> page = new Page<>(pageNumber, 10);
        IPage<ConsultData> consultDataList = consultTableService.getAllConsult(page);
        if( consultDataList != null ){
            return Result.success("查询成功！", consultDataList);
        }
        return Result.error("查询失败");
    }

    @PostMapping("/reply")
    public Result<Object> replyConsult(HttpServletRequest req, @RequestBody Consult consult){
        Claims claims = getHttpServletRequestJwt(req);
        if (getAuthority(claims,2)) {
            return Result.error("对不起，您的权限不足！");
        }

        emailService.sendSimpleMail(consult.getEmail(), "咨询回复",consult.getContent());
        UpdateWrapper<Consult> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("consult_id", consult.getConsultId());
        updateWrapper.set("reply_time", LocalDate.now());
        updateWrapper.set("state",1);
        boolean ifSuccess = consultTableService.update(updateWrapper);

        if( ifSuccess ){
            return Result.success("回复成功！", null);
        }
        return Result.error("回复失败");
    }
}
