package com.atguigu.yygh.common.handler;


import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public R handleException(Exception ex) {
        ex.printStackTrace();
        log.error(ex.getMessage());
        return R.error().message(ex.getMessage());
    }


    @ExceptionHandler(value = {SQLException.class}) // 细粒度异常处理
    public R handleSqlException(SQLException ex){
        ex.printStackTrace();
        log.error(ex.getMessage());
        return R.error().message("Sql异常");
    }

    @ExceptionHandler(value = {ArithmeticException.class}) // 细粒度异常处理
    public R handleArithmeticException(ArithmeticException ex){
        ex.printStackTrace();
        log.error(ex.getMessage());
        return R.error().message("数字异常");
    }


//    @ExceptionHandler(value = {ArithmeticException.class}) // 细粒度异常处理
//    public R handleRuntimeException(RuntimeException sqlException){
//        sqlException.printStackTrace();
//        return R.error().message("编译时异常");
//    }

    @ExceptionHandler(value = {YyghException.class}) // 细粒度异常处理
    public R handleYyghException(YyghException ex){
        ex.printStackTrace();
        log.error(ex.getMessage());
        return R.error().message(ex.getMessage()).code(ex.getCode());
    }

}
