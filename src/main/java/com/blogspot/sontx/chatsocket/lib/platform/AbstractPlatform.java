package com.blogspot.sontx.chatsocket.lib.platform;

import com.blogspot.sontx.chatsocket.lib.thread.ThreadInvoker;
import com.google.common.eventbus.EventBus;
import lombok.Getter;

public abstract class AbstractPlatform implements Platform {
    @Getter
    private final ThreadInvoker threadInvoker;
    private final EventBus eventBus;

    protected AbstractPlatform(ThreadInvoker threadInvoker) {
        this.threadInvoker = threadInvoker;
        eventBus = new EventBus(getClass().getName());
    }

    public void attach(Object service) {
        eventBus.register(service);
    }

    public void detach(Object service) {
        eventBus.unregister(service);
    }

    public void postEvent(Object event) {
        eventBus.post(event);
    }

    @Override
    public void exit() {
        System.exit(0);
    }
}
