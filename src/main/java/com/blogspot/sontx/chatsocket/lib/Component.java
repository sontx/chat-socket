package com.blogspot.sontx.chatsocket.lib;

import com.blogspot.sontx.chatsocket.lib.platform.Platform;
import com.blogspot.sontx.chatsocket.lib.settings.SettingsLoader;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class Component {
    @Setter
    @Getter(AccessLevel.PROTECTED)
    private Platform platform;

    protected <T> T getSetting(Class<T> settingType) {
        return platform.getSettings().get(settingType);
    }

    protected void post(Object event) {
        platform.postEvent(event);
    }

    protected void runOnUiThread(Runnable callback) {
        runOnUiThread(callback, true);
    }

    protected void runOnUiThread(Runnable callback, boolean wait) {
        if (wait)
            platform.getThreadInvoker().invokeAndWait(callback);
        else
            platform.getThreadInvoker().invokeLater(callback);
    }

    protected void runAsync(Runnable callback) {
        platform.getThreadInvoker().executeAsync(callback);
    }
}
