package com.lb.im.common.cache.lock;

import java.util.concurrent.TimeUnit;

public interface DistributedLock {

    boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;

    boolean tryLock(long waitTime, TimeUnit unit) throws InterruptedException;

    boolean tryLock() throws InterruptedException;

    void lock(long leaseTime, TimeUnit unit);

    void unlock();

    boolean isLocked();

    boolean isHeldByCurrentThread();

    boolean isHeldByThread(long threadId);

}
