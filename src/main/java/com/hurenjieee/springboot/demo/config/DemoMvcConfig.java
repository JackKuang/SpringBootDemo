package com.hurenjieee.springboot.demo.config;

import com.hurenjieee.springboot.demo.interceptor.RecordInterceptor;
import com.ulisesbocchio.jasyptspringboot.annotation.ConditionalOnMissingBean;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Jack
 * @date 2019/3/6 16:59
 */

@EnableEncryptableProperties
@Configuration
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



    @Bean
    public MatherService matherService(){
        //调用同一个bean
        return new MatherService(sonService());
    }

    @Bean
    public FatherService fatherService(){
        //调用同一个bean
        return new FatherService(sonService());
    }

    @Bean SonService sonService(){
        //只会执行一次
        return  new SonService();
    }



    class MatherService{

        SonService sonService;

        public MatherService(SonService sonService) {
            this.sonService = sonService;
            System.out.println("motherSerivce init"+sonService);
        }
    }

    class FatherService{
        SonService sonService;

        public FatherService(SonService sonService) {
            this.sonService = sonService;
            System.out.println("fatherService init"+sonService);
        }
    }

    class SonService{
        public SonService() {
            System.out.println("sonService init");
        }
    }
}
