package com.blogspot.sontx.chatsocket.lib.service;

import com.blogspot.sontx.chatsocket.lib.service.event.ShowMessageBoxEvent;
import com.blogspot.sontx.chatsocket.lib.service.message.MessageBox;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MessageBoxService extends AbstractService {
    private final MessageBox messageBox;

    public MessageBoxService(MessageBox messageBox) {
        this.messageBox = messageBox;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageBox(ShowMessageBoxEvent event) {
        messageBox.show(event.getCaption(), event.getMessage(), event.getType());
    }
}
