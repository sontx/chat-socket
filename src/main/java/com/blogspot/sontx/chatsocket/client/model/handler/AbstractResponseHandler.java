package com.blogspot.sontx.chatsocket.client.model.handler;

import com.blogspot.sontx.chatsocket.lib.service.AbstractService;
import com.blogspot.sontx.chatsocket.lib.service.message.MessageType;

abstract class AbstractResponseHandler extends AbstractService implements ResponseHandler {
    void showErrorMessage(String message, Object reason) {
        postMessageBox(message + ": " + reason, MessageType.Error);
    }
}
