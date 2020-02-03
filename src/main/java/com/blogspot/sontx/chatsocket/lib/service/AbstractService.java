package com.blogspot.sontx.chatsocket.lib.service;

import com.blogspot.sontx.chatsocket.AppConfig;
import com.blogspot.sontx.chatsocket.lib.Component;
import com.blogspot.sontx.chatsocket.lib.platform.Platform;
import com.blogspot.sontx.chatsocket.lib.service.event.ShowMessageBoxEvent;
import com.blogspot.sontx.chatsocket.lib.service.event.StopServiceEvent;
import com.blogspot.sontx.chatsocket.lib.service.message.MessageType;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractService extends Component implements Service {
    private static AtomicInteger freeId = new AtomicInteger();

    @Getter
    private volatile boolean started;

    @Getter
    private int id;

    private boolean registered = false;

    protected AbstractService() {
        id = freeId.incrementAndGet();
    }

    @Override
    public synchronized void start() {
        if (!registered) {
            getPlatform().attach(this);
            registered = true;
        }
        started = true;
    }

    @Override
    public synchronized void stop() {
        if (registered) {
            getPlatform().detach(this);
            registered = false;
        }
        started = false;
    }

    protected void postMessageBox(String message, String caption, MessageType type) {
        post(new ShowMessageBoxEvent(message, caption, type));
    }

    protected void postMessageBox(String message, MessageType type) {
        postMessageBox(message, AppConfig.getDefault().getAppName(), type);
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
