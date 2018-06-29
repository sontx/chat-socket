package com.blogspot.sontx.chatsocket.lib.service;

import com.blogspot.sontx.chatsocket.AppConfig;
import com.blogspot.sontx.chatsocket.lib.platform.Platform;
import com.blogspot.sontx.chatsocket.lib.service.event.ShowMessageBoxEvent;
import com.blogspot.sontx.chatsocket.lib.service.event.StopServiceEvent;
import com.blogspot.sontx.chatsocket.lib.service.message.MessageType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractService implements Service {
    private static AtomicInteger freeId = new AtomicInteger();

    @Setter
    @Getter(AccessLevel.PROTECTED)
    private Platform platform;

    @Getter
    private volatile boolean started;

    @Getter
    private int id;

    protected AbstractService() {
        id = freeId.incrementAndGet();
    }

    @Override
    public synchronized void start() {
        EventBus eventBus = platform.getEventBus();
        if (eventBus != null && !eventBus.isRegistered(this))
            eventBus.register(this);
        started = true;
    }

    @Override
    public synchronized void stop() {
        EventBus eventBus = platform.getEventBus();
        if (eventBus != null && eventBus.isRegistered(this))
            eventBus.unregister(this);
        started = false;
    }

    protected void postMessageBox(String message, String caption, MessageType type) {
        post(new ShowMessageBoxEvent(message, caption, type));
    }

    protected void postMessageBox(String message, MessageType type) {
        postMessageBox(message, AppConfig.getDefault().getAppName(), type);
    }

    protected void post(Object event) {
        EventBus eventBus = platform.getEventBus();
        if (eventBus != null)
            eventBus.post(event);
    }

    @SuppressWarnings("unchecked")
    public <T> T build(Platform platform) {
        setPlatform(platform);
        return (T) this;
    }

    @Subscribe
    public void onStopService(StopServiceEvent event) {
        if (event.getId() == id) {
            stop();
        }
    }
}
