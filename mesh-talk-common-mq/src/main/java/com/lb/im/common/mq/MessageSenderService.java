package com.lb.im.common.mq;

import com.lb.im.common.domain.model.TopicMessage;
import org.apache.rocketmq.client.producer.TransactionSendResult;

/**
 * 消息发送服务接口，提供消息发送功能。
 */
public interface MessageSenderService {

    /**
     * 发送消息
     */
    boolean send(TopicMessage message);

    /**
     * 发送事务消息到RocketMQ。
     *
     * @param message 事务消息对象
     * @return 事务消息发送结果，包含消息ID、偏移量等元数据信息
     */
    default TransactionSendResult sendMessageTransaction(TopicMessage message, Object arg) {
        return null;
    }

}
