package com.itheima.health.exception;

/**
 * <p>
 *  自定异常：
 *    1. 友好的提示
 *    2. 区分异常的类型（业务，系统，未知）
 *    3. 终止已知不符合业务逻辑代码的继续执行
 * </p>
 *
 * @author: Eric
 * @since: 2021/1/6
 */
public class MyException extends RuntimeException {
    public MyException(String message){
        super(message);
    }
}
