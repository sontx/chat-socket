package com.blogspot.sontx.chatsocket.lib.platform;

import com.blogspot.sontx.chatsocket.lib.service.MessageBoxService;
import com.blogspot.sontx.chatsocket.lib.service.message.SwingMessageBox;
import com.blogspot.sontx.chatsocket.lib.thread.SwingThreadInvoker;
import com.blogspot.sontx.chatsocket.lib.view.WindowUtils;

public abstract class SwingPlatform extends AbstractPlatform {
    protected SwingPlatform() {
        super(new SwingThreadInvoker());
        WindowUtils.setSystemLookAndFeel();
        MessageBoxService messageBoxService = new MessageBoxService(new SwingMessageBox());
        messageBoxService.setPlatform(this);
        messageBoxService.start();
    }
}
