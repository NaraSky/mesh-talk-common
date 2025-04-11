package com.lb.im.common.cache.lock.factory;

import com.lb.im.common.cache.lock.DistributedLock;

/**
 * 分布式锁工程接口
 */
public interface DistributedLockFactory {

    /**
     * 获取分布式锁
     *
     * @param lockKey 锁的key
     * @return 分布式锁
     */
    DistributedLock getDistributedLock(String lockKey);

}
