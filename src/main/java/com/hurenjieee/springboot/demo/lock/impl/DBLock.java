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

    /**
     * init Sql
     * CREATE TABLE `db_lock` (
     *   `id` bigint(20) NOT NULL AUTO_INCREMENT,
     *   `lock_key` varchar(255) DEFAULT NULL COMMENT 'Key',
     *   `lock_value` varchar(255) DEFAULT NULL COMMENT 'Value',
     *   `create_time` datetime DEFAULT NULL,
     *   PRIMARY KEY (`id`),
     *   UNIQUE KEY `u_lock_key` (`lock_key`) USING BTREE COMMENT '唯一索引'
     * ) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
     */
    @Resource
    DbLockMapper dbLockMapper;

    @Override
    public synchronized boolean lock(String key, String value, Long time) {
        try {
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
                    .eq(DbLock::getLockValue, value);
            int updateNum = dbLockMapper.delete(wrapper);
            if (updateNum > 0) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}
