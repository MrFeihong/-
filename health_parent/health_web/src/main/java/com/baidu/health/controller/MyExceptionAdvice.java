package com.baidu.health.controller;

import com.baidu.health.entity.Result;
import com.baidu.health.exceptions.BusinessException;
import com.baidu.health.exceptions.SysException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


/**
 * log日志：
 *       info：记录流程的内容，对重要业务时。
 *       debug：记录重要的数据id，key
 *       error：记录异常信息 代替system.out，e.printStackTrace();
 */

@RestControllerAdvice
public class MyExceptionAdvice {

    private static final Logger log = LoggerFactory.getLogger(MyExceptionAdvice.class);

    @ExceptionHandler({BusinessException.class})
    public Result handleBusinessException(BusinessException be){
        return new Result(false,be.getMessage());
    }

    @ExceptionHandler({SysException.class})
    public Result handleSysException(SysException be){
        log.error("System exception happen.",be);
        return new Result(false,"System error,Please try again later!");
    }

    @ExceptionHandler(ArithmeticException.class)
    public ModelAndView handleArithmeticException(ArithmeticException be){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error");
        // 设置提示信息
        mv.addObject("msg",be.getMessage());
        return mv;
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception be){
        log.error("Unknown exception happen.",be);
        return new Result(false,"Unknown exception happen, please contact our administrator!");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException e) {
        return new Result(false, "没有权限");
    }


    /**
     * 提交的参数校验提示信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        //校验没通过的字段
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        if (null !=fieldErrors){
            StringBuilder sb =new StringBuilder();
            for (FieldError fieldError : fieldErrors) {
//                fieldError.getField()+":"+
                sb.append(fieldError.getDefaultMessage());
                sb.append(":");
            }
            if (sb.length()>0){
                sb.setLength(sb.length()-1);
            }
            return new Result(false,sb.toString());
        }
        return new Result(false,"参数校验失败！");
    }
}
