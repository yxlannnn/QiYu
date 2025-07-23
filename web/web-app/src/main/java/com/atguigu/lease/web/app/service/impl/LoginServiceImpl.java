package com.atguigu.lease.web.app.service.impl;

import com.atguigu.lease.common.constant.RedisConstant;
import com.atguigu.lease.common.exception.LeaseException;
import com.atguigu.lease.common.result.ResultCodeEnum;
import com.atguigu.lease.common.utils.CodeUtil;
import com.atguigu.lease.common.utils.JwtUtil;
import com.atguigu.lease.model.entity.UserInfo;
import com.atguigu.lease.model.enums.BaseStatus;
import com.atguigu.lease.web.app.mapper.UserInfoMapper;
import com.atguigu.lease.web.app.service.LoginService;
import com.atguigu.lease.web.app.service.SmsService;
import com.atguigu.lease.web.app.vo.user.LoginVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);
    @Autowired
    private SmsService smsService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserInfoMapper userInfoMapper;

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

    @Override
    public String login(LoginVo loginVo) {

        //判断用户输入的手机号是否为空
        if (loginVo.getPhone() == null){
           throw new LeaseException(ResultCodeEnum.APP_LOGIN_PHONE_EMPTY);
        }

        //判断用户输入的验证码是否为空
        if (loginVo.getCode() == null){
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_CODE_EMPTY);
        }

        //判断验证码是否过期
        String key = RedisConstant.APP_LOGIN_PREFIX+loginVo.getPhone();
        String code = redisTemplate.opsForValue().get(key);
        if (code == null){
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_CODE_EXPIRED);
        }

        //如果不为空，判断用户输入的验证码和redis中的验证码是否一样
        if (!code.equals(loginVo.getCode())){
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_CODE_ERROR);
        }

        //验证码通过
        //开始从数据库查询信息，并校验用户信息
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getPhone,loginVo.getPhone());
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        //判断查询出来的用户是否为null
        //为空说明是新用户，直接注册
        //不为空说明是已注册用户，判断是否被禁用

        if (userInfo==null){
            //注册
            userInfo = new UserInfo();
            userInfo.setPhone(loginVo.getPhone());
            userInfo.setStatus(BaseStatus.ENABLE);
            userInfo.setNickname("用户-"+loginVo.getPhone().substring(7));
            userInfoMapper.insert(userInfo);
        }else {
            //判断是否是被禁用的用户
            if (userInfo.getStatus()==BaseStatus.DISABLE){
                throw new LeaseException(ResultCodeEnum.APP_ACCOUNT_DISABLED_ERROR);
            }
        }


        //登陆成功返回一个token
        return JwtUtil.createToken(userInfo.getId(),userInfo.getPhone());
    }
}
