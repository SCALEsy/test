package com.example.test.config;

import com.example.test.aop.httplimit.HttpLimitException;
import com.example.test.bean.result.Result;
import com.example.test.bean.result.ResultType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


//@RestControllerAdvice(basePackages ="com.example.test.rest")//这个只会监听springmvc的错误，不会监听jersey的错误
public class MyExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    //@ResponseBody
    public Result<String> runtimeException(RuntimeException e){
        return new Result<>(ResultType.FAILURE,e.getMessage());
    }
    @ExceptionHandler(value = HttpLimitException.class)
    //@ResponseBody
    public Result<String> httplimit(HttpLimitException e){
        return new Result<>(ResultType.FAILURE,e.getMessage());
    }
}
