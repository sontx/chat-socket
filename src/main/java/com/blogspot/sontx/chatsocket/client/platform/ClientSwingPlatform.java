package com.blogspot.sontx.chatsocket.client.platform;

import com.blogspot.sontx.chatsocket.client.view.swing.SwingViewFactory;
import com.blogspot.sontx.chatsocket.lib.Platform;
import com.blogspot.sontx.chatsocket.lib.thread.Invoker;
import com.blogspot.sontx.chatsocket.lib.thread.SwingInvoker;
import com.blogspot.sontx.chatsocket.lib.view.MessageBox;
import com.blogspot.sontx.chatsocket.lib.view.ViewFactory;
import lombok.Getter;

public class ClientSwingPlatform implements Platform {
    @Getter
    private final ViewFactory viewFactory;
    @Getter
    private final Invoker invoker;

    public ClientSwingPlatform() {
        viewFactory = new SwingViewFactory();
        invoker = new SwingInvoker();
        MessageBox.setInvoker(invoker);
    }
}
