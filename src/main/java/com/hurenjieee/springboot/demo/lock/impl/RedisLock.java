package com.hurenjieee.springboot.demo.lock.impl;

import com.hurenjieee.springboot.demo.lock.Lock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;


/**
 * 数据库分布式锁（非Lua脚本处理）
 * FIXME Jackson2JsonRedisSerializer保存在redis中的时候，会多出一个双引号
 *
 * @author Jack
 * @date 2019/5/6 11:19
 */
@Service
public class RedisLock implements Lock {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    private static final Long SUCCESS = 1L;


    @Override
    public boolean lock(String key, String value, Long time) {
        System.out.println("Redis lock:" + key);
        return redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.MILLISECONDS);
//        String script = "if redis.call('setNx',KEYS[1],ARGV[1]) then if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end end";
//        RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
//        Long result = redisTemplate.execute(redisScript, Collections.singletonList(key),value,time);
//        return 1L == result;
    }

    @Override
    public boolean unlock(String key, String value) {
        RedisScript<Long> luaScript = new DefaultRedisScript<>(RELEASE_LOCK_SCRIPT, Long.class);
        Object result = redisTemplate.execute(luaScript, Collections.singletonList(key), value);
        return SUCCESS.equals(result);
    }
}
