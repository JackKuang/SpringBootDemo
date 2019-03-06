package com.hurenjieee.springboot.demo.service.impl;

import com.hurenjieee.springboot.demo.entity.Test;
import com.hurenjieee.springboot.demo.mapper.TestMapper;
import com.hurenjieee.springboot.demo.service.ITestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jack
 * @since 2019-03-06
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements ITestService {

}
