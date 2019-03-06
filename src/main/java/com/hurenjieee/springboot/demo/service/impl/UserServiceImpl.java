package com.hurenjieee.springboot.demo.service.impl;

import com.hurenjieee.springboot.demo.entity.User;
import com.hurenjieee.springboot.demo.mapper.UserMapper;
import com.hurenjieee.springboot.demo.service.IUserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
