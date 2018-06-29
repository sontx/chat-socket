package com.blogspot.sontx.chatsocket.server.platform;

import com.blogspot.sontx.chatsocket.lib.platform.SwingPlatform;
import com.blogspot.sontx.chatsocket.lib.view.ViewFactory;
import com.blogspot.sontx.chatsocket.server.view.swing.SwingViewFactory;
import lombok.Getter;

public class ServerSwingPlatform extends SwingPlatform {
    @Getter
    private final ViewFactory viewFactory;

    public ServerSwingPlatform() {
        viewFactory = new SwingViewFactory();
    }
}
