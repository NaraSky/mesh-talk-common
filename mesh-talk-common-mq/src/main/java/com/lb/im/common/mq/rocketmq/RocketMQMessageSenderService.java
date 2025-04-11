package com.lb.im.common.mq.rocketmq;

import com.alibaba.fastjson.JSONObject;
import com.lb.im.common.domain.constans.IMConstants;
import com.lb.im.common.domain.model.TopicMessage;
import com.lb.im.common.mq.MessageSenderService;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "message.mq.type", havingValue = "rocketmq")
public class RocketMQMessageSenderService implements MessageSenderService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public boolean send(TopicMessage message) {
        try {
            SendResult sendResult = rocketMQTemplate.syncSend(message.getDestination(), this.getMessage(message));
            return SendStatus.SEND_OK.equals(sendResult.getSendStatus());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public TransactionSendResult sendMessageTransaction(TopicMessage message, Object arg) {
        return rocketMQTemplate.sendMessageInTransaction(message.getDestination(), this.getMessage(message), arg);
    }

    //构建RocketMQ发送的消息
    private Message<String> getMessage(TopicMessage message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(IMConstants.MSG_KEY, message);
        return MessageBuilder.withPayload(jsonObject.toJSONString()).build();
    }
}
