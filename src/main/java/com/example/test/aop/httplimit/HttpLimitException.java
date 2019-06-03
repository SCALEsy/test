package com.example.test.aop.httplimit;

public class HttpLimitException extends RuntimeException {
    public HttpLimitException(String msg){
        super(msg);
    }
}
