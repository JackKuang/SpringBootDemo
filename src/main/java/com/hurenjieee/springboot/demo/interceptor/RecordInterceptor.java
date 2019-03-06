package com.hurenjieee.springboot.demo.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 时间记录拦截器
 * @author Jack
 * @date 2018/9/2 15:01
 */
public class RecordInterceptor implements HandlerInterceptor {

    public static final String START_TIME = "startTime";
    private Marker recordMarker = MarkerManager.getMarker("RECORD");
    private Logger logger = LogManager.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME,startTime);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (Long)request.getAttribute(START_TIME);
        request.removeAttribute(START_TIME);
        long endTime = System.currentTimeMillis();
        logger.info(recordMarker,"URL:" + request.getRequestURL() + ";time:" + (endTime - startTime));
    }
}
