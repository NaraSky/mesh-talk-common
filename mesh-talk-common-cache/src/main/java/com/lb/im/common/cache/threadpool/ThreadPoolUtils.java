package com.lb.im.common.cache.threadpool;

import java.util.concurrent.*;

public class ThreadPoolUtils {

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(16,
                                                                        16,
                                                                        30,
                                                                        TimeUnit.SECONDS,
                                                                        new ArrayBlockingQueue<>(4096),
                                                                        new ThreadPoolExecutor.CallerRunsPolicy());

    public static void execute(Runnable command) {
        executor.execute(command);
    }

    public static <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }

    public static void shutdown() {
        if (executor != null) {
            executor.shutdown();
        }
    }
}
