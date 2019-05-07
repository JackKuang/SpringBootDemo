package com.hurenjieee.springboot.demo.lock.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hurenjieee.springboot.demo.entity.DbLock;
import com.hurenjieee.springboot.demo.lock.ILock;
import com.hurenjieee.springboot.demo.mapper.DbLockMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 数据库分布式锁
 * 增加synchronized只能避免本地不会造成锁冲突，分布式依旧会冲突。
 * 但是redis分布式锁却不会
 *
 * @author Jack
 * @date 2019/5/6 11:19
 */
@Service("dbLock")
public class DBLock implements ILock {

    @Resource
    DbLockMapper dbLockMapper;

    @Override
    public synchronized boolean lock(String key, String value, Long time) {
        try {
            QueryWrapper<DbLock> wrapper = new QueryWrapper<DbLock>();
            wrapper.lambda().eq(DbLock::getLockKey, key)
                    .eq(DbLock::getLockStatus, 1);
            int count = dbLockMapper.selectCount(wrapper);
            if (count > 0) {
                return false;
            }
            DbLock lock = new DbLock(key, value);
            int insertNum = dbLockMapper.insert(lock);
            if (insertNum > 0) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public synchronized boolean unlock(String key, String value) {
        try {
            QueryWrapper<DbLock> wrapper = new QueryWrapper<DbLock>();
            wrapper.lambda().eq(DbLock::getLockKey, key)
                    .eq(DbLock::getLockValue, value)
                    .eq(DbLock::getLockStatus, 1);
            DbLock lock = dbLockMapper.selectOne(wrapper);
            if (lock == null) {
                return false;
            }
            lock.setLockStatus(2);
            int updateNum = dbLockMapper.updateById(lock);
            if (updateNum > 0) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}
