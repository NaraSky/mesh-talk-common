package com.lb.im.common.domain.event;

import com.lb.im.common.domain.model.TopicMessage;

public class IMBaseEvent extends TopicMessage {

    private Long id;

    public IMBaseEvent() {
    }

    public IMBaseEvent(Long id, String destination) {
        super(destination);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
