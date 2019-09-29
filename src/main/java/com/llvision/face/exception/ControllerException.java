package com.llvision.face.exception;

import com.alibaba.fastjson.JSONObject;
import com.llvision.face.vo.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;


@RestControllerAdvice
public class ControllerException {


    /**
     * 请求参数验证不通过将调用这个方法
     * 作用@RequestBody()
     * @param exception
     * @return
     */
    @ExceptionHandler( MethodArgumentNotValidException.class)
    public Result constraintViolationException(HttpServletRequest request, MethodArgumentNotValidException exception){
        Result result = new Result();
        result.setData(new JSONObject());
        RequestContext requestContext = new RequestContext(request);
        result.setMsg(requestContext.getMessage("qqsb"));
        result.setCode(0);
        return result;
    }
}
