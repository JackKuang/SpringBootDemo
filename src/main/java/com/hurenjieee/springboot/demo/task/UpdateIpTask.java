package com.hurenjieee.springboot.demo.task;

import com.hurenjieee.springboot.demo.service.IUpdateIpService;
import com.hurenjieee.springboot.demo.util.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 更新IP的定时任务
 * @author Jack
 * @date 2019/5/5 15:04
 */
@Component
public class UpdateIpTask {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private IUpdateIpService updateIpService;

    public static final String LAST_IP = "LAST_IP";

    @Scheduled(fixedDelay = 1000,initialDelay = 10000)
    public void updateIp() throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        String oldIpString = (String)redisTemplate.opsForValue().get(LAST_IP);
        String ipString = IPUtil.getIP();
        if (true) {
            redisTemplate.opsForValue().set(LAST_IP,ipString);
            updateIpService.updateIp(ipString);
        }
    }
}
