package com.hurenjieee.springboot.demo.controller;

import com.hurenjieee.springboot.demo.lock.Lock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lenovo
 * @date 2019/5/6 14:33
 */
@RestController
@RequestMapping("/demo/lock")
public class LockController {

    @Autowired
    Lock redisLock;

    @RequestMapping("/lock/{key}/{value}")
    @ResponseBody
    public boolean lock(@PathVariable String key,@PathVariable String value){
        return redisLock.lock(key,value,600000L);
    }

    @RequestMapping("/unlock/{key}/{value}")
    @ResponseBody
    public boolean unlock(@PathVariable String key,@PathVariable String value){
        return redisLock.unlock(key,value);
    }

}
