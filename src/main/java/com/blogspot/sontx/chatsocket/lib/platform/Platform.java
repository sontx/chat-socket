package com.blogspot.sontx.chatsocket.lib.platform;

import com.blogspot.sontx.chatsocket.lib.settings.SettingsLoader;
import com.blogspot.sontx.chatsocket.lib.thread.ThreadInvoker;
import com.blogspot.sontx.chatsocket.lib.view.ViewFactory;

public interface Platform {
    ViewFactory getViewFactory();

    ThreadInvoker getThreadInvoker();

    SettingsLoader getSettings();

    void attach(Object service);

    void detach(Object service);

    void postEvent(Object event);

    void exit();
}
