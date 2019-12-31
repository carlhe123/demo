package com.carl.demo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author carl.he
 * @Date 2019/12/13
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private Integer code;
    private String message;
    private T data;

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 返回成功消息
     * @return Result
     */
    public static <T> Result<T> ok() {
        return new Result(ResultCode.OK.getCode(), ResultCode.OK.getMsg());
    }

    /**
     * 返回成功消息
     * @return Result
     */
    public static <T> Result<T> ok(String message) {
        return new Result(ResultCode.OK.getCode(), message);
    }

    /**
     * 返回成功消息
     * @return Result
     */
    public static <T> Result<T> ok(T data) {
        return new Result(ResultCode.OK.getCode(), ResultCode.OK.getMsg(), data);
    }

    /**
     * 返回成功消息
     * @return Result
     */
    public static <T> Result<T> ok(String message, T data) {
        return new Result(ResultCode.OK.getCode(), message, data);
    }

    /**
     * 返回失败消息
     * @return Result
     */
    public static <T> Result<T> fail(){
        return new Result(ResultCode.FAIL.getCode(),ResultCode.FAIL.getMsg());
    }

    /**
     * 返回失败消息
     * @return Result
     */
    public static <T> Result<T> fail(String message){
        return new Result(ResultCode.FAIL.getCode(),message);
    }

    /**
     * 返回失败消息
     * @return Result
     */
    public static <T> Result<T> fail(Integer code,String message){
        return new Result(code,message);
    }

    /**
     * 返回异常消息
     * @return Result
     */
    public static <T> Result<T> error() {
        return new Result(ResultCode.ERROR.getCode(), ResultCode.ERROR.getMsg());
    }

    /**
     * 返回异常消息
     * @return Result
     */
    public static <T> Result<T> error(String message) {
        return new Result(ResultCode.ERROR.getCode(), message);
    }

    /**
     * 返回异常消息
     * @return Result
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result(code, message);
    }

    /**
     * 返回远程调用失败
     * @return Result
     */
    public static <T> Result<T> rpcError() {
        return new Result(ResultCode.RPC_ERROR.getCode(), ResultCode.RPC_ERROR.getMsg());
    }

    /**
     * 返回远程调用失败
     * @return Result
     */
    public static <T> Result<T> rpcError(String message) {
        return new Result(ResultCode.RPC_ERROR.getCode(), message);
    }

    /**
     * 返回远程调用失败
     * @return Result
     */
    public static <T> Result<T> rpcError(Integer code,String message) {
        return new Result(code, message);
    }
}
