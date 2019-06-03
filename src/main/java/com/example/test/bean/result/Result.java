package com.example.test.bean.result;

import java.io.Serializable;

public class Result<T> implements Serializable {
    private int code;
    private String msg;
    private T data;

    public Result() {

    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(ResultType type, T data) {
        this.code = type.getCode();
        this.msg = type.getMsg();
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
