package com.blogspot.sontx.chatsocket.client.model.handler;

import com.blogspot.sontx.chatsocket.client.event.RegisteredEvent;
import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.bean.Response;
import com.blogspot.sontx.chatsocket.lib.bean.ResponseCode;
import com.blogspot.sontx.chatsocket.lib.bo.ObjectTransmission;

public class RegisterResponseHandler extends AbstractResponseHandler {
    @Override
    public void handle(ObjectTransmission transmission, Response response) throws Exception {
        if (response.getCode() == ResponseCode.OK) {
            post(new RegisteredEvent((AccountInfo) response.getExtra()));
        } else {
            showErrorMessage("Can not register new account", response.getExtra());
        }
    }
}
