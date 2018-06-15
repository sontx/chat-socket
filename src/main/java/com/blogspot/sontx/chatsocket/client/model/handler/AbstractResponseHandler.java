package com.blogspot.sontx.chatsocket.client.model.handler;

import com.blogspot.sontx.chatsocket.lib.view.MessageBox;

abstract class AbstractResponseHandler implements ResponseHandler {
    void showErrorMessage(String message) {
        MessageBox.showInUIThread(null, message, MessageBox.MESSAGE_ERROR);
    }

    void showErrorMessage(String message, Object reason) {
        MessageBox.showInUIThread(null, message + ": " + reason, MessageBox.MESSAGE_ERROR);
    }
}
