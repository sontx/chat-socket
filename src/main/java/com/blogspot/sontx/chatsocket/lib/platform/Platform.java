package com.blogspot.sontx.chatsocket.lib.platform;

import com.blogspot.sontx.chatsocket.lib.thread.Invoker;
import com.blogspot.sontx.chatsocket.lib.view.ViewFactory;
import org.greenrobot.eventbus.EventBus;

public interface Platform {
    ViewFactory getViewFactory();

    Invoker getInvoker();

    EventBus getEventBus();

    void exit();
}
