package com.hurenjieee.springboot.demo.lock;

public interface ILock {

    /**
     * 获取锁
     * @param key
     * @return
     */
    public boolean lock(String key, String value, Long time);

    /**
     * 释放锁
     * @param key
     * @return
     */
    public boolean unlock(String key, String value);

}
