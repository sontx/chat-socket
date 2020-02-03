package com.blogspot.sontx.chatsocket.lib.platform;

import com.blogspot.sontx.chatsocket.lib.thread.ThreadInvoker;
import com.blogspot.sontx.chatsocket.lib.view.ViewFactory;

public interface Platform {
    ViewFactory getViewFactory();

    ThreadInvoker getThreadInvoker();

    void attach(Object service);

    void detach(Object service);

    void postEvent(Object event);

    void exit();
}
