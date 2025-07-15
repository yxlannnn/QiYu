package com.atguigu.lease.common.exception;


import com.atguigu.lease.common.result.ResultCodeEnum;
import lombok.Data;

//TODO 自定义全局异常
//再在全局异常处理器中定义一个异常处理器，专门处理我们自定义的全局异常
@Data
public class LeaseException extends RuntimeException{

    private Integer code;

    public LeaseException(Integer code,String message){
        super(message);
//        super.message=message;
        this.code=code;


    }

    public LeaseException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code= resultCodeEnum.getCode();


    }
}
