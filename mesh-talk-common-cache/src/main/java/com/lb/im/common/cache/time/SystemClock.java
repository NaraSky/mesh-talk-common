package com.lb.im.common.cache.time;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 系统时钟类，提供精确到指定间隔的时间戳。
 * 通过定期更新机制维护当前时间，确保时间戳在指定精度内有效。
 */
public class SystemClock {

    // 系统时钟线程名称标识符
    private static final String THREAD_NAME = "system.clock";

    // 基于毫秒精度的系统时钟实例
    private static final SystemClock MILLIS_CLOCK = new SystemClock(1);

    // 时间精度值，单位为毫秒
    private final long precision;

    // 原子操作支持的当前时间戳
    private final AtomicLong now;

    /**
     * 初始化系统时钟，以指定的精度定期更新当前时间。
     * @param precision 时间更新的间隔（毫秒），决定时间戳的精度。
     */
    public SystemClock(long precision) {
        this.precision = precision;
        now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }

    /**
     * 创建并启动定时任务线程，定期更新当前时间戳。
     * 使用单线程调度器，以指定的精度间隔执行时间更新操作。
     */
    private void scheduleClockUpdating() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, THREAD_NAME);
            thread.setDaemon(true);
            return thread;
        });
        executor.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), precision, precision, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取单例的毫秒精度系统时钟实例。
     * 实例的时间更新间隔为1毫秒。
     * @return 毫秒精度的系统时钟实例。
     */
    public static SystemClock millisClock() {
        return MILLIS_CLOCK;
    }

    /**
     * 获取当前时间戳。
     * 时间戳基于最近一次更新的系统时间，精度由构造时指定。
     * @return 当前时间戳（毫秒）。
     */
    public long now() {
        return now.get();
    }
}
