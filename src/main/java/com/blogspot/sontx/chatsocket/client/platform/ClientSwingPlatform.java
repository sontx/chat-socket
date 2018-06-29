package com.blogspot.sontx.chatsocket.client.platform;

import com.blogspot.sontx.chatsocket.client.view.swing.SwingViewFactory;
import com.blogspot.sontx.chatsocket.lib.platform.SwingPlatform;
import com.blogspot.sontx.chatsocket.lib.view.ViewFactory;
import lombok.Getter;

public class ClientSwingPlatform extends SwingPlatform {
    @Getter
    private final ViewFactory viewFactory;

    public ClientSwingPlatform() {
        viewFactory = new SwingViewFactory();
    }
}
