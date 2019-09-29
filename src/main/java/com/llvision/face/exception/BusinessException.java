package com.llvision.face.exception;

import com.llvision.face.common.ResponseDTO;
import lombok.Getter;

/**
 * 自定义业务异常
 * @Author yudong
 * @Date 2018年07月03日 上午8:31
 */
public class BusinessException extends RuntimeException{

    @Getter
    private ResponseDTO response;

    /**
     * 构建一个消息和响应信息
     * @param message  消息
     * @param response 建议使用常量 R
     */
    public BusinessException(ResponseDTO response, String message) {
        super(message);
        this.response = response;
    }

    /**
     * 构建一个响应信息
     * @param response 建议使用常量 R
     */
    public BusinessException(ResponseDTO response) {
    	super("");
        this.response = response;
    }
}
