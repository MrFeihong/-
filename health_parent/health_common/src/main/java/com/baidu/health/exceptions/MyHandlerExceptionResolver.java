package com.baidu.health.exceptions;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 统一异常处理
 * </p>
 */
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    /**
     * Controller报异常时，就会调用这个方法
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error");
        if(ex instanceof BusinessException){
            // 业务异常
            BusinessException be = (BusinessException)ex;
            // 设置提示信息
            mv.addObject("msg",be.getMessage());
        }else if(ex instanceof SysException){
            // 系统异常
            SysException be = (SysException)ex;
            // 设置提示信息
            mv.addObject("msg","太多人来访问了，请稍后重试");
            // 不可以直接， 使用log;
            be.printStackTrace();
        }else{
            mv.addObject("msg","系统发生未知异常，请联系管理员");
            ex.printStackTrace();
        }
        return mv;
    }
}
