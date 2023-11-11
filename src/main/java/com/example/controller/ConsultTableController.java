package com.example.controller;

import com.example.common.Result;
import com.example.entity.Consult;
import com.example.service.ConsultTableService;
import com.example.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consult")
@Slf4j
public class ConsultTableController {
    @Autowired
    private ConsultTableService consultTableService;

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

    private Claims getHttpServletRequestJwt(HttpServletRequest req){
        String jwt = req.getHeader("Token");
        return JwtUtils.parseJWT(jwt);
    }
}
