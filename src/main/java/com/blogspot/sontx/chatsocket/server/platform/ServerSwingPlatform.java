package com.blogspot.sontx.chatsocket.server.platform;

import com.blogspot.sontx.chatsocket.lib.view.MessageBox;
import com.blogspot.sontx.chatsocket.server.view.swing.SwingViewFactory;
import com.blogspot.sontx.chatsocket.lib.Platform;
import com.blogspot.sontx.chatsocket.lib.thread.Invoker;
import com.blogspot.sontx.chatsocket.lib.thread.SwingInvoker;
import com.blogspot.sontx.chatsocket.lib.view.ViewFactory;
import lombok.Getter;

public class ServerSwingPlatform implements Platform {
    @Getter
    private final ViewFactory viewFactory;
    @Getter
    private final Invoker invoker;

    public ServerSwingPlatform() {
        viewFactory = new SwingViewFactory();
        invoker = new SwingInvoker();
        MessageBox.setInvoker(invoker);
    }
}
