package com.lb.im.common.domain.model;

import com.lb.im.common.domain.enums.IMTerminalType;

import java.util.LinkedList;
import java.util.List;

public class IMGroupMessage<T>{

    /**
     * 发消息的用户
     */
    private IMUserInfo sender;

    /**
     * 接收者id列表(群成员列表,为空则不会推送)
     */
    private List<Long> receiveIds  = new LinkedList<>();

    /**
     * 接收者终端类型，默认全部终端
     */
    private List<Integer> receiveTerminals = IMTerminalType.getAllCode();

    /**
     * 是否发送给自己的其他终端,默认true
     */
    private Boolean sendToSelf = true;

    /**
     * 是否需要回推发送结果,默认true
     */
    private Boolean sendResult = true;

    /**
     *  消息内容
     */
    private T data;

    public IMGroupMessage() {
    }

    public IMGroupMessage(IMUserInfo sender, List<Long> receiveIds, List<Integer> receiveTerminals, Boolean sendToSelf, Boolean sendResult, T data) {
        this.sender = sender;
        this.receiveIds = receiveIds;
        this.receiveTerminals = receiveTerminals;
        this.sendToSelf = sendToSelf;
        this.sendResult = sendResult;
        this.data = data;
    }


    public IMUserInfo getSender() {
        return sender;
    }

    public void setSender(IMUserInfo sender) {
        this.sender = sender;
    }

    public List<Long> getReceiveIds() {
        return receiveIds;
    }

    public void setReceiveIds(List<Long> receiveIds) {
        this.receiveIds = receiveIds;
    }

    public List<Integer> getReceiveTerminals() {
        return receiveTerminals;
    }

    public void setReceiveTerminals(List<Integer> receiveTerminals) {
        this.receiveTerminals = receiveTerminals;
    }

    public Boolean getSendToSelf() {
        return sendToSelf;
    }

    public void setSendToSelf(Boolean sendToSelf) {
        this.sendToSelf = sendToSelf;
    }

    public Boolean getSendResult() {
        return sendResult;
    }

    public void setSendResult(Boolean sendResult) {
        this.sendResult = sendResult;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
