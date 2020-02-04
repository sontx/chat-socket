package com.blogspot.sontx.chatsocket.lib.platform;

import com.blogspot.sontx.chatsocket.lib.settings.JsonSettingsLoader;
import com.blogspot.sontx.chatsocket.lib.settings.SettingsLoader;
import com.blogspot.sontx.chatsocket.lib.thread.ThreadInvoker;
import com.google.common.eventbus.EventBus;
import lombok.Getter;

public abstract class AbstractPlatform implements Platform {
    @Getter
    private final ThreadInvoker threadInvoker;
    private final EventBus eventBus;
    @Getter
    private final SettingsLoader settings;

    protected AbstractPlatform(ThreadInvoker threadInvoker) {
        this.threadInvoker = threadInvoker;
        eventBus = new EventBus(getClass().getName());
        settings = new JsonSettingsLoader("app.json");
    }

    @Override
    public void attach(Object service) {
        eventBus.register(service);
    }

    @Override
    public void detach(Object service) {
        eventBus.unregister(service);
    }

    @Override
    public void postEvent(Object event) {
        eventBus.post(event);
    }

    @Override
    public void exit() {
        settings.save();
        System.exit(0);
    }
}
