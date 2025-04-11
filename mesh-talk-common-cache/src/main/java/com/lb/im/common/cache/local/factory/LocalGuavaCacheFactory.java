package com.lb.im.common.cache.local.factory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * 本地Guava缓存工厂类，提供创建不同配置的Guava缓存实例的方法。
 */
public class LocalGuavaCacheFactory {

    /**
     * 创建一个本地Guava缓存实例，使用预设配置。
     *
     * @return 配置好的缓存实例，初始容量为200，最大并发级别为5，缓存项在写入后300秒过期。
     */
    public static <K, V> Cache<K, V> getLocalCache() {
        // 配置缓存参数：初始容量200，最大并发级别5，过期时间300秒
        return CacheBuilder.newBuilder()
                .initialCapacity(200)
                .concurrencyLevel(5)
                .expireAfterWrite(300, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 根据指定的过期时间创建本地Guava缓存实例。
     *
     * @param duration 缓存项过期时间（秒）
     * @return 配置好的缓存实例，初始容量为200，最大并发级别为5，缓存项在写入后指定duration秒过期。
     */
    public static <K, V> Cache<K, V> getLocalCache(long duration) {
        // 配置缓存参数：初始容量200，最大并发级别5，过期时间由duration参数指定
        return CacheBuilder.newBuilder()
                .initialCapacity(200)
                .concurrencyLevel(5)
                .expireAfterWrite(duration, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 根据指定的初始容量和过期时间创建本地Guava缓存实例。
     *
     * @param initialCapacity 缓存初始容量
     * @param duration        缓存项过期时间（秒）
     * @return 配置好的缓存实例，初始容量由参数指定，最大并发级别为5，缓存项在写入后指定duration秒过期。
     */
    public static <K, V> Cache<K, V> getLocalCache(int initialCapacity, long duration) {
        // 配置缓存参数：初始容量和过期时间由参数指定，保持并发级别为5
        return CacheBuilder.newBuilder()
                .initialCapacity(initialCapacity)
                .concurrencyLevel(5)
                .expireAfterWrite(duration, TimeUnit.SECONDS)
                .build();
    }

}
