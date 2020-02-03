package com.blogspot.sontx.chatsocket.lib.platform;

import com.blogspot.sontx.chatsocket.lib.service.MessageBoxService;
import com.blogspot.sontx.chatsocket.lib.service.message.JavaFxMessageBox;
import com.blogspot.sontx.chatsocket.lib.thread.JavaFxThreadInvoker;

public abstract class JavaFxPlatform extends AbstractPlatform {
    protected JavaFxPlatform() {
        super(new JavaFxThreadInvoker());
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
