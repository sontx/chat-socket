package com.blogspot.sontx.chatsocket.lib.platform;

import com.blogspot.sontx.chatsocket.lib.service.MessageBoxService;
import com.blogspot.sontx.chatsocket.lib.service.message.JavaFxMessageBox;
import com.blogspot.sontx.chatsocket.lib.thread.JavaFxInvoker;
import org.greenrobot.eventbus.support.JavaFxMainThreadSupport;

public abstract class JavaFxPlatform extends AbstractPlatform {
    protected JavaFxPlatform() {
        setInvoker(new JavaFxInvoker());
        initializeEventBus(new JavaFxMainThreadSupport());

        MessageBoxService messageBoxService = new MessageBoxService(new JavaFxMessageBox());
        messageBoxService.setPlatform(this);
        messageBoxService.start();
    }

    @Override
    public void exit() {
        javafx.application.Platform.exit();
        super.exit();
    }
}
