package com.hurenjieee.springboot.demo.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jack
 * @since 2019-03-06
 */
@RestController
@RequestMapping("/demo/test")
public class TestController {


    @RequestMapping("/exception")
    public void exception(){
        int i = 1/0;
    }
}
