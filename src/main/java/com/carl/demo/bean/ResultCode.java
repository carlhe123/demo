package com.carl.demo.bean;

public enum ResultCode {
    FAIL(0,"失败"),
    OK(1,"成功"),
    ERROR(2,"程序异常"),
    RPC_ERROR(3,"远程调用失败");

    private Integer code;
    private String msg;

    ResultCode(Integer code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
