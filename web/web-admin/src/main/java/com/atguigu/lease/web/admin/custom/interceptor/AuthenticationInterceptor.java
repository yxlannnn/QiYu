package com.atguigu.lease.web.admin.custom.interceptor;

import com.atguigu.lease.common.exception.LeaseException;
import com.atguigu.lease.common.login.LoginUser;
import com.atguigu.lease.common.login.LoginUserHolder;
import com.atguigu.lease.common.result.ResultCodeEnum;
import com.atguigu.lease.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    //TODO 在Controller层的接口处理请求之前先判断Token是否有效

    /**
     *
     * @param request 前端请求 包含请求头，请求体，请求参数等等
     * @param response 后端响应信息
     * @param handler 拦截的Controller层的方法
     * @return true为放行，false为拦截，请求的处理到此为止
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // TODO 处理请求request的请求头当中的token，token一般放在请求头中，存储为k-v健值对的形式
        String token = request.getHeader("access-token");

        Claims claims = JwtUtil.parseToken(token);
        //TODO 在拦截器中拿出解析后的token中的userid和username，并存入ThreadLocal
        Long userId = claims.get("userId", Long.class);
        String userName = claims.get("userName", String.class);
        //这边是已经存入ThreadLocal中了
        LoginUserHolder.setLoginUser(new LoginUser(userId,userName));
        //由于SpringMVC使用的是线程池技术，当一个线程处理完请求就会回到线程池中，
        // 这个线程的所有本地变量不会消失，因此需要手动清理线程中的用户信息



        //为什么这里可以直接return true
        //因为上面的解析token方法，如果没有解析成功，在里面报错，就会直接被全局异常处理器给捕获，不会执行return语句
        //直接就是返回异常，不会继续往下走了
        //如果能走到这一步，说明解析没问题，可以直接返回true
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //请求处理完之后清理线程中的用户信息
        LoginUserHolder.clear();
    }
}
