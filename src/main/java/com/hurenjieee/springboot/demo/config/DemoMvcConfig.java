package com.hurenjieee.springboot.demo.config;

import com.hurenjieee.springboot.demo.interceptor.RecordInterceptor;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Jack
 * @date 2019/3/6 16:59
 */

@EnableEncryptableProperties
@Configuration
@EnableScheduling
@MapperScan({"com.hurenjieee.springboot.demo.mapper"})
public class DemoMvcConfig implements WebMvcConfigurer {

    /**
     * 附加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getRecordInterceptor());
    }

    /**
     * 过滤器注入
     *
     * @return
     */
    @Bean
    public RecordInterceptor getRecordInterceptor(){
        return new RecordInterceptor();
    }
}
