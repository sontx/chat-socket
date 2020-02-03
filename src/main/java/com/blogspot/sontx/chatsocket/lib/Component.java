package com.blogspot.sontx.chatsocket.lib;

import com.blogspot.sontx.chatsocket.lib.platform.Platform;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class Component {
    @Setter
    @Getter(AccessLevel.PROTECTED)
    private Platform platform;

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
