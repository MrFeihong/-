package com.baidu.health.exceptions;

/**
 * <p>
 * 自定异常：系统异常
 * </p>
 *
 */
public class SysException extends RuntimeException{
    public SysException(String message){
        super(message);
    }
}
