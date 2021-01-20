package com.itheima.health.exception;

/**
 * <p>
 * 自定异常：业务异常,程序来抛出
 * </p>
 *
 */
public class BusinessException extends RuntimeException{
    // 提示信息
    public BusinessException(String message){
        super(message);
    }
}
