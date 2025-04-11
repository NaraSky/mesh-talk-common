package com.lb.im.common.cache.model;

import com.lb.im.common.cache.model.base.IMCommonCache;

/**
 * 业务相关的缓存模型，继承自通用缓存基类IMCommonCache，使用泛型T存储具体业务内容。
 * 提供链式设置方法，方便配置缓存内容、版本及重试策略。
 */
public class IMBusinessCache<T> extends IMCommonCache {

    private T content;

    /**
     * 设置缓存内容并标记缓存存在。
     *
     * @param content 缓存的具体业务内容
     * @return 当前IMBusinessCache实例，支持链式调用
     */
    public IMBusinessCache<T> with(T content) {
        this.content = content;
        this.exists = true;
        return this;
    }

    /**
     * 设置缓存版本号。
     *
     * @param version 缓存内容的版本号
     * @return 当前IMBusinessCache实例，支持链式调用
     */
    public IMBusinessCache<T> withVersion(Long version) {
        this.version = version;
        return this;
    }

    /**
     * 标记该缓存需要稍后重试处理。
     *
     * @return 当前IMBusinessCache实例，支持链式调用
     */
    public IMBusinessCache<T> retryLater() {
        this.retryLater = true;
        return this;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
