package com.llvision.face.advice;

import com.llvision.face.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;


/**
 * 处理返回值 添加接口版本
 * @Author guoyc
 */
@Slf4j
@ControllerAdvice
public class ResponseBodyAdvice implements org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice{

    @Value("${faceService.api.version}")
    private String apiVersion;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
         try {
             if (body instanceof Result) {
                 Result result = (Result) body;
                 result.setApiVision(apiVersion);
             }
         }catch (Exception e){
            e.printStackTrace();
            log.debug(e.getLocalizedMessage());
         }
        return body;
    }


}
