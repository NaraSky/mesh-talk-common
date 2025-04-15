package com.lb.im.common.mq.event.local;

import com.alibaba.cola.event.EventBusI;
import com.lb.im.common.domain.model.TopicMessage;
import com.lb.im.common.mq.event.MessageEventSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "message.mq.event.type", havingValue = "cola")
public class LocalMessageEventSenderService implements MessageEventSenderService {

    @Autowired
    private EventBusI eventBus;

    @Override
    public boolean send(TopicMessage message) {
        eventBus.fire(message);
        return true;
    }
}
