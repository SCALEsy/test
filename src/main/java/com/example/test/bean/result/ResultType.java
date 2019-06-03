package com.example.test.bean.result;

public enum ResultType {
    SUCCESS(200,"OK"),
    FAILURE(200,"FAILURE");
    private int code;
    private String msg;
    ResultType(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
