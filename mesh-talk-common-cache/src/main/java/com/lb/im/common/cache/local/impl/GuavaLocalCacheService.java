package com.lb.im.common.cache.local.impl;

import com.google.common.cache.Cache;
import com.lb.im.common.cache.local.LocalCacheService;
import com.lb.im.common.cache.local.factory.LocalGuavaCacheFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "cache.type.local", havingValue = "guava")
public class GuavaLocalCacheService<K, V> implements LocalCacheService<K, V> {

    private final Cache<K, V> cache = LocalGuavaCacheFactory.getLocalCache();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public V getIfPresent(K key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void remove(K key) {
        cache.invalidate(key);
    }
}
