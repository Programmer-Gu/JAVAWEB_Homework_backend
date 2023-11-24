package com.example.utils.Servicelogic;

import com.example.common.Result;
import com.example.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

public class BaseInfoGet {
    public static Claims getHttpServletRequestJwt(HttpServletRequest req){
        String jwt = req.getHeader("Token");
        return JwtUtils.parseJWT(jwt);
    }

    public static boolean getAuthority(Claims claims, int authorityLimit){
        return (int) claims.get("authority") > authorityLimit;
    }
}
