package com.hurenjieee.springboot.demo.controller;

import com.hurenjieee.springboot.demo.lock.ILock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Lenovo
 * @date 2019/5/6 14:33
 */
@RestController
@RequestMapping("/demo/lock")
public class LockController {

    @Resource(name = "dbLock")
//    ILock redisLock;
    ILock dbLock;

    @RequestMapping("/lock/{key}/{value}")
    @ResponseBody
    public boolean lock(@PathVariable String key,@PathVariable String value){
        return dbLock.lock(key,value,6000L);
    }

    @RequestMapping("/unlock/{key}/{value}")
    @ResponseBody
    public boolean unlock(@PathVariable String key,@PathVariable String value){
        return dbLock.unlock(key,value);
    }

}
