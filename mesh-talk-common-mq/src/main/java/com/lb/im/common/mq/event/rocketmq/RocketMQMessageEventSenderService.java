package com.lb.im.common.mq.event.rocketmq;

import com.alibaba.fastjson.JSONObject;
import com.lb.im.common.domain.constans.IMConstants;
import com.lb.im.common.domain.model.TopicMessage;
import com.lb.im.common.mq.event.MessageEventSenderService;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * RocketMQ消息事件发送服务实现类
 * 在配置中message.mq.event.type设置为rocketmq时启用
 * 实现消息发送功能，支持普通消息和事务消息
 */
@Component
@ConditionalOnProperty(name = "message.mq.event.type", havingValue = "rocketmq")
public class RocketMQMessageEventSenderService implements MessageEventSenderService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送普通消息
     * @param message 消息对象，包含目标topic和消息内容
     * @return 发送状态是否成功
     */
    @Override
    public boolean send(TopicMessage message) {
        try {
            // 发送消息并获取发送结果
            SendResult sendResult = rocketMQTemplate.syncSend(message.getDestination(), this.getMessage(message));
            return SendStatus.SEND_OK.equals(sendResult.getSendStatus());
        } catch (Exception e) {
            // 发生异常时返回发送失败
            return false;
        }
    }

    /**
     * 发送事务消息
     * @param message 消息对象，包含目标topic和消息内容
     * @param arg 事务扩展参数
     * @return 事务消息发送结果对象
     */
    @Override
    public TransactionSendResult sendMessageInTransaction(TopicMessage message, Object arg) {
        return rocketMQTemplate.sendMessageInTransaction(message.getDestination(), this.getMessage(message), arg);
    }

    /**
     * 构建RocketMQ消息对象
     * @param message 消息内容对象
     * @return 包含JSON格式消息体的Spring Message对象
     */
    private Message<String> getMessage(TopicMessage message) {
        // 将消息内容转换为JSON格式
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(IMConstants.MSG_KEY, message);
        return MessageBuilder.withPayload(jsonObject.toJSONString()).build();
    }
}
