package com.hurenjieee.springboot.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hurenjieee.springboot.demo.entity.User;
import com.hurenjieee.springboot.demo.mapper.UserMapper;
import com.hurenjieee.springboot.demo.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jack
 * @since 2019-03-06
 */
@RestController
@RequestMapping("/demo/user")
public class UserController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserServiceImpl userService;

    @RequestMapping("/list")
    public List<User> list(){
        return  userMapper.selectList(new QueryWrapper<User>());
    }

    @RequestMapping("/list2")
    public List<User> list2(){
        return  userService.list();
    }

}
