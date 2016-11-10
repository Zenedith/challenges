package com.goeuro.service.configuration.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomAsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        logger.warn("Exception message: {}", ex.getMessage());
        logger.info("Method name: {}", method.getName());

        for (Object param : params) {
            logger.info("Parameter value: {}", param);
        }
    }

}