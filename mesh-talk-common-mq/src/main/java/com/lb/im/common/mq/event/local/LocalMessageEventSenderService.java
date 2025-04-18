package com.lb.im.common.mq.event.local;

import com.alibaba.cola.event.EventBusI;
import com.lb.im.common.domain.model.TopicMessage;
import com.lb.im.common.mq.event.MessageEventSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 本地消息事件发送服务实现类，基于COLA事件总线进行消息发送。
 * 该服务仅在配置属性message.mq.event.type为"cola"时生效。
 * 实现MessageEventSenderService接口，负责将消息事件发布到事件总线。
 */
@Component
@ConditionalOnProperty(name = "message.mq.event.type", havingValue = "cola")
public class LocalMessageEventSenderService implements MessageEventSenderService {

    // EventBusI 是 COLA框架提供的事件总线接口，其核心作用是发布和订阅事件，实现应用内的事件驱动通信。
    @Autowired
    private EventBusI eventBus;

    /**
     * 发送消息事件到COLA事件总线。
     *
     * @param message 需要发送的TopicMessage消息对象
     * @return 布尔值，总是返回true表示发送成功（当前实现中不处理发送失败情况）
     */
    @Override
    public boolean send(TopicMessage message) {
        eventBus.fire(message);
        return true;
    }
}
