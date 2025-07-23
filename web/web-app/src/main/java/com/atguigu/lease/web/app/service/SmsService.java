package com.atguigu.lease.web.app.service;

public interface SmsService {
    //发送短信验证码的业务逻辑
    void sendCode(String phone,String code);

}
