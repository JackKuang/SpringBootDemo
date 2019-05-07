package com.hurenjieee.springboot.demo.service.impl;

import com.hurenjieee.springboot.demo.entity.DbLock;
import com.hurenjieee.springboot.demo.mapper.DbLockMapper;
import com.hurenjieee.springboot.demo.service.IDbLockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jack
 * @since 2019-05-07
 */
@Service
public class DbLockServiceImpl extends ServiceImpl<DbLockMapper, DbLock> implements IDbLockService {

}
