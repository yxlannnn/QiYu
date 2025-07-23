package com.atguigu.lease.web.app.service.impl;

import com.atguigu.lease.common.constant.RedisConstant;
import com.atguigu.lease.common.exception.LeaseException;
import com.atguigu.lease.common.result.ResultCodeEnum;
import com.atguigu.lease.common.utils.CodeUtil;
import com.atguigu.lease.web.app.service.LoginService;
import com.atguigu.lease.web.app.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SmsService smsService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void getCode(String phone) {

        //使用随机验证码生成器，生成一个6位的随机验证码，然后用sms服务发出去
        String code = CodeUtil.getRandomCode(6);
        String key  = RedisConstant.APP_LOGIN_PREFIX+phone;

        //TODO 控制短信发送速度，一分钟一条的速率
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey){
            //getExpire用于获取key的ttl
            Long ttl = redisTemplate.getExpire(key,TimeUnit.SECONDS);
            if (RedisConstant.APP_LOGIN_CODE_TTL_SEC - ttl < RedisConstant.APP_LOGIN_CODE_RESEND_TIME_SEC) {
                //若存在时间不足一分钟，响应发送过于频繁
                throw new LeaseException(ResultCodeEnum.APP_SEND_SMS_TOO_OFTEN);
            }
        }


        smsService.sendCode(phone,code);
        //发完短信将验证码存入redis
        //key值为手机号
        //opsForValue是处理健值对形式的redis数据
        redisTemplate.opsForValue().set(key,code,RedisConstant.APP_LOGIN_CODE_TTL_SEC, TimeUnit.SECONDS);



    }
}
