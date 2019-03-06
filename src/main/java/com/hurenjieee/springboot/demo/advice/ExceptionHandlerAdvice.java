package com.hurenjieee.springboot.demo.advice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理类
 * @author Jack
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    public static final String START_TIME = "startTime";
    private Marker recordMarker = MarkerManager.getMarker("RECORD");

    private Marker errorMarker = MarkerManager.getMarker("ERROR");
    private Logger logger = LogManager.getLogger(getClass());

    /**
     * 异常处理
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String exception(Exception e, WebRequest request) {
        logger.error(errorMarker,"【异常】",e);
            HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        long startTime = (Long)httpServletRequest.getAttribute(START_TIME);
        httpServletRequest.removeAttribute(START_TIME);
        long endTime = System.currentTimeMillis();
        logger.info(recordMarker,"URL:" + httpServletRequest.getRequestURL() + ";time:" + (endTime - startTime));
        return "error";
    }

}
