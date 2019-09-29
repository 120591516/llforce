package com.llvision.face.mqtt.processor;

/**
 * 接收消息处理
 */
public interface Processor<T> {

    /**
     * 是否能处理
     * @param t
     * @return
     */
    boolean isProcessing(T t);

    /**
     * 处理
     * @param t
     */
    void processing(T t);

}
