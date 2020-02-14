package com.wherewego.train.Interceptor;

import com.wherewego.train.entity.ResponseData;
import com.wherewego.train.exception.WhereWeGoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常拦截
 * @Author:lubeilin
 * @Date:Created in 19:33 2020/2/7
 * @Modified By:
 */
@ControllerAdvice
public class ExceptionInterceptor {
    private static Logger logger = LoggerFactory.getLogger(ExceptionInterceptor.class);
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseData exc(Exception e) {
        if (e instanceof WhereWeGoException){
            return new ResponseData(((WhereWeGoException)e).getCode(),e.getMessage());
        }
        logger.warn(e.getMessage(),e);
        return new ResponseData(500,"未知错误");
    }
}
