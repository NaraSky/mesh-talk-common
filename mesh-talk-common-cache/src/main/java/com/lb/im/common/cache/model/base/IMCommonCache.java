package com.lb.im.common.cache.model.base;

import java.io.Serializable;

/**
 * 通用缓存模型
 */
public class IMCommonCache implements Serializable {

    private static final long serialVersionUID = 4618395732008113736L;

    protected boolean exists;
    protected Long version;
    protected boolean retryLater;

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public boolean isRetryLater() {
        return retryLater;
    }

    public void setRetryLater(boolean retryLater) {
        this.retryLater = retryLater;
    }
}
