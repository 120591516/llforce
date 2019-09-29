package com.llvision.face.mqtt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.llvision.face.mqtt.processor.Processor;
import com.llvision.face.utils.GenericsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;


/**
 * 消息处理器
 * @Author yudong
 * @Date 2018年12月27日 17:10
 */
@Slf4j
public class MessageObjectHandler implements MessageHandler {

    private Collection<Processor> processor;

    private final static ObjectMapper OM = new ObjectMapper();

    public MessageObjectHandler(Collection<Processor> processor) {
        this.processor = processor;
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        if (isJson(message.getPayload().toString())) {
            Iterator<Processor> iterator = processor.iterator();
            while (iterator.hasNext()) {
                Processor processor = iterator.next();
                Class clazz = GenericsUtils.getGenericInterfaces(processor.getClass());
                try {
                    Object data = OM.readValue(message.getPayload().toString(), clazz);
                    boolean isProcessor = processor.isProcessing(data);
                    if (isProcessor) {
                        processor.processing(data);
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 判断是不是json
     * @param json
     * @return
     */
    private boolean isJson(String json){
        try {
            OM.readTree(json);
            return true;
        } catch (IOException e) {
            log.debug(e.getMessage());
            return false;
        }
    }

}
