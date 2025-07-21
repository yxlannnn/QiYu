package com.atguigu.lease.common.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

//jwt工具类，用于生成token和解析token
public class JwtUtil {

    private static SecretKey secretKey = Keys.hmacShaKeyFor("BydklAszRBDSd7CymYzJN36dWZ9VBR2j".getBytes());


    //TODO 生成token的方法
    public static String createToken(Long userId,String userName){
        //TODO 使用jjwt-api工具类创建jwt工具类 用于后续移动端和pc端的jwt令牌的生成
        String jwt = Jwts.builder()
                .setExpiration(new Date(System. currentTimeMillis() + 3600000))
                .setSubject("LOGIN_USER")
                .claim("userId",userId)
                .claim("userName",userName)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }

    //TODO 解析token的方法

}
