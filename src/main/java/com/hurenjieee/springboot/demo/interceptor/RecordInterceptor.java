package com.hurenjieee.springboot.demo.interceptor;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 时间记录拦截器
 *
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
        request.setAttribute(START_TIME, startTime);
        logger.info(recordMarker, "SessionId:{};RequestId:{};URL:{};param:{}", request.getSession().getId(), startTime, request.getRequestURL(), JSON.toJSONString(request.getParameterMap()));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (Long) request.getAttribute(START_TIME);
        request.removeAttribute(START_TIME);
        long endTime = System.currentTimeMillis();
        logger.info(recordMarker, "SessionId:{};RequestId:{};ExecuteTime:{}", request.getSession().getId(), startTime, request, endTime - startTime);
    }
}
