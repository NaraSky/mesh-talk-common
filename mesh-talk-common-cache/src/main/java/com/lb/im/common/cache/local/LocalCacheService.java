package com.lb.im.common.cache.local;

public interface LocalCacheService<K, V> {

    void put(K key, V value);

    V getIfPresent(K key);

    void remove(K key);
}
