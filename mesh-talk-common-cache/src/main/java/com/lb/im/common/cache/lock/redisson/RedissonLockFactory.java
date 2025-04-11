package com.lb.im.common.cache.lock.redisson;

import com.lb.im.common.cache.lock.DistributedLock;
import com.lb.im.common.cache.lock.factory.DistributedLockFactory;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnProperty(name = "distribute.lock.type", havingValue = "redisson")
public class RedissonLockFactory implements DistributedLockFactory {

    private final Logger logger = LoggerFactory.getLogger(RedissonLockFactory.class);

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public DistributedLock getDistributedLock(String key) {
        /**
         * 根据指定键获取分布式锁实例。
         *
         * @param key 锁的唯一标识符
         * @return 基于Redisson客户端的分布式锁实例
         */
        RLock rLock = redissonClient.getLock(key);
        return new DistributedLock() {
            @Override
            public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
                /**
                 * 尝试在指定时间内获取锁，并设置锁的持有时间。
                 *
                 * @param waitTime  最大等待时间（单位由timeUnit指定）
                 * @param leaseTime 锁的持有时间，超时后自动释放
                 * @param unit      时间单位
                 * @return 是否成功获取锁
                 */
                boolean isLockSuccess = rLock.tryLock(waitTime, leaseTime, unit);
                logger.info("{} get lock result:{}", key, isLockSuccess);
                return isLockSuccess;
            }

            @Override
            public boolean tryLock(long waitTime, TimeUnit unit) throws InterruptedException {
                return rLock.tryLock(waitTime, unit);
            }

            @Override
            public boolean tryLock() throws InterruptedException {
                return rLock.tryLock();
            }

            @Override
            public void lock(long leaseTime, TimeUnit unit) {
                /**
                 * 以指定持有时间锁定资源。
                 *
                 * @param leaseTime 锁的持有时间
                 * @param unit      时间单位
                 */
                rLock.lock(leaseTime, unit);
            }

            @Override
            public void unlock() {
                /**
                 * 仅当锁被当前线程持有时释放锁，确保线程安全。
                 */
                if (isLocked() && isHeldByCurrentThread()) {
                    rLock.unlock();
                }
            }

            @Override
            public boolean isLocked() {
                return rLock.isLocked();
            }

            @Override
            public boolean isHeldByThread(long threadId) {
                return rLock.isHeldByThread(threadId);
            }

            @Override
            public boolean isHeldByCurrentThread() {
                return rLock.isHeldByCurrentThread();
            }
        };
    }
}