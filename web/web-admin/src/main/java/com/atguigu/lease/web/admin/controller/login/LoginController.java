package com.atguigu.lease.web.admin.controller.login;


import com.atguigu.lease.common.login.LoginUser;
import com.atguigu.lease.common.login.LoginUserHolder;
import com.atguigu.lease.common.result.Result;
import com.atguigu.lease.common.utils.JwtUtil;
import com.atguigu.lease.web.admin.mapper.SystemUserMapper;
import com.atguigu.lease.web.admin.service.LoginService;
import com.atguigu.lease.web.admin.vo.login.CaptchaVo;
import com.atguigu.lease.web.admin.vo.login.LoginVo;
import com.atguigu.lease.web.admin.vo.system.user.SystemUserInfoVo;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台管理系统登录管理")
@RestController
@RequestMapping("/admin")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private LoginService service;

    @Autowired
    private SystemUserMapper systemUserMapper;

    @Operation(summary = "获取图形验证码")
    @GetMapping("login/captcha")
    public Result<CaptchaVo> getCaptcha() {
        //TODO 获取图形验证码 用base64算法将二进制数字转换成字符串，将字符串传给前端，浏览器自动解析成图片

        CaptchaVo result = service.getCaptcha();
        return Result.ok(result);
    }

    @Operation(summary = "登录")
    @PostMapping("login")
    public Result<String> login(@RequestBody LoginVo loginVo) {

        //TODO 校验用户登陆请求
        //校验三个东西，redis中的验证码，用户登陆状态（查看是否被禁用），密码
        String jwt = service.login(loginVo);
        return Result.ok(jwt);

    }

    @Operation(summary = "获取登陆用户个人信息")
    @GetMapping("info")
    public Result<SystemUserInfoVo> info() {
//        Claims claims = JwtUtil.parseToken(token);
//        Long userId = claims.get("userId", Long.class);
        //不需要二次解析token了，直接在ThreadLocal中获取就行
        LoginUser loginUser = LoginUserHolder.getLoginUser();
        Long userId = loginUser.getUserId();

        SystemUserInfoVo systemUserInfoVo = service.getLoginUserInfoById(userId);


        return Result.ok(systemUserInfoVo);
    }
}