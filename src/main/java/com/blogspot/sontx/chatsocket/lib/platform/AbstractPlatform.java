package com.blogspot.sontx.chatsocket.lib.platform;

import com.blogspot.sontx.chatsocket.lib.thread.Invoker;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.MainThreadSupport;

public abstract class AbstractPlatform implements Platform {
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Invoker invoker;

    @Getter
    private EventBus eventBus;

    void initializeEventBus(MainThreadSupport mainThreadSupport) {
        eventBus = EventBus
                .builder()
                .throwSubscriberException(false)
                .mainThreadSupport(mainThreadSupport)
                .build();
    }

    @Override
    public void exit() {
        System.exit(0);
    }
}
