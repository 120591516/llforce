package com.llvision.face.utils;

import com.llvision.face.constants.Constants;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.MDC;


/**
 * @Author: guoyc
 * @Date: 2019/5/20 16:45
 * @Version 1.0
 */
public class LogUtil {
    public static void traceLogId() {
        MDC.put(Constants.TRACE_LOG_ID, RandomStringUtils.randomNumeric(12));
        MDC.put(Constants.METHOD_START_TIME,System.currentTimeMillis() + "");
    }
}
