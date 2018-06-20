package com.blogspot.sontx.chatsocket.client.model.handler;

import com.blogspot.sontx.chatsocket.client.event.LoggedEvent;
import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.bean.Response;
import com.blogspot.sontx.chatsocket.lib.bean.ResponseCode;
import com.blogspot.sontx.chatsocket.lib.bo.ObjectTransmission;
import org.greenrobot.eventbus.EventBus;

public class LoginResponseHandler extends AbstractResponseHandler {
    @Override
    public void handle(ObjectTransmission transmission, Response response) throws Exception {
        if (response.getCode() == ResponseCode.OK) {
            EventBus.getDefault().post(new LoggedEvent((AccountInfo) response.getExtra()));
        } else {
            showErrorMessage("Can not login", response.getExtra());
        }
    }
}