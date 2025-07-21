package com.atguigu.lease.common.utils;

import com.atguigu.lease.common.exception.LeaseException;
import com.atguigu.lease.common.result.ResultCodeEnum;
import io.jsonwebtoken.*;
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
    public static void parseToken(String token){

        //先判断token是否为空
        if (token == null){
            throw new LeaseException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }

        try{
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
            //解析方法如果抛异常，说明token解析有问题，直接抛出catch中的异常
            jwtParser.parseClaimsJws(token);
        }catch (ExpiredJwtException e){
            throw new LeaseException(ResultCodeEnum.TOKEN_EXPIRED);

        }catch (JwtException e){
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);

        }

    }

    //TODO 不过期token生成！！！可以用这个方法，修改一下上面的过期时间，在360000后面*24*365L就可以生成一个一年不过期的token
    public static void main(String[] args) {
        System.out.println(createToken(2L,"user"));
    }

}
