package com.atguigu.lease.common.exception;


import com.atguigu.lease.common.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    //TODO 全局异常处理的意思就是，只要整个过程中发生了与@ExceptionHandler(Exception.class)中的方法类似的异常，就会执行方法中的处理方法
    //不管是在整个过程中的什么地方，都会被捕获到
    //所以service层可以向上抛出异常给controller层
    //但是一般controller层不应该继续往上抛异常
    //有了全局异常处理器，就可以随便抛异常
    //反正都会被捕获


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handle( Exception e){
        e.printStackTrace();


        return Result.fail();
    }
}
