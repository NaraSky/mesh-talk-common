package com.lb.im.common.mq.event;

import com.lb.im.common.domain.model.TopicMessage;
import org.apache.rocketmq.client.producer.TransactionSendResult;

public interface MessageEventSenderService {

    /**
     * 发送消息
     * @param message 发送的消息
     */
    boolean send(TopicMessage message);

    /**
     * 发送事务消息，主要是RocketMQ
     * @param message 事务消息
     * @param arg 其他参数
     * @return 返回事务发送结果
     */
    default TransactionSendResult sendMessageInTransaction(TopicMessage message, Object arg){
        return null;
    }
}
