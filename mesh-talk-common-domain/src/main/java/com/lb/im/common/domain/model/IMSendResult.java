package com.lb.im.common.domain.model;

import java.util.List;

/**
 * 响应结果数据模型
 */
public class IMSendResult<T> extends TopicMessage {

    private static final long serialVersionUID = 6255973991282308490L;

    // 发送的用户
    private IMUserInfo fromUser;
    // 接收的用户
    private IMUserInfo receiver;
    // 指令类型
    private Integer code;
    private T data;

    public IMSendResult() {
    }

    public IMSendResult(IMUserInfo fromUser, IMUserInfo receiver, Integer code, T data) {
        this.fromUser = fromUser;
        this.receiver = receiver;
        this.code = code;
        this.data = data;
    }

    public IMUserInfo getFromUser() {
        return fromUser;
    }

    public void setFromUser(IMUserInfo fromUser) {
        this.fromUser = fromUser;
    }

    public IMUserInfo getReceiver() {
        return receiver;
    }

    public void setReceiver(IMUserInfo receiver) {
        this.receiver = receiver;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
