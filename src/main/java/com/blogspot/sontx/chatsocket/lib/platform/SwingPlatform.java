package com.blogspot.sontx.chatsocket.lib.platform;

import com.blogspot.sontx.chatsocket.lib.service.MessageBoxService;
import com.blogspot.sontx.chatsocket.lib.service.message.SwingMessageBox;
import com.blogspot.sontx.chatsocket.lib.thread.SwingInvoker;
import com.blogspot.sontx.chatsocket.lib.view.WindowUtils;
import org.greenrobot.eventbus.support.SwingMainThreadSupport;

public abstract class SwingPlatform extends AbstractPlatform {
    protected SwingPlatform() {
        WindowUtils.setSystemLookAndFeel();

        setInvoker(new SwingInvoker());
        initializeEventBus(new SwingMainThreadSupport());

        MessageBoxService messageBoxService = new MessageBoxService(new SwingMessageBox());
        messageBoxService.setPlatform(this);
        messageBoxService.start();
    }
}
