package com.blogspot.sontx.chatsocket.client.model.handler;

import com.blogspot.sontx.chatsocket.client.event.MyAccountInfoReceivedEvent;
import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.bean.Response;
import com.blogspot.sontx.chatsocket.lib.bean.ResponseCode;
import com.blogspot.sontx.chatsocket.lib.bo.ObjectTransmission;
import org.greenrobot.eventbus.EventBus;

public class AccountInfoResponseHandler extends AbstractResponseHandler {
    @Override
    public void handle(ObjectTransmission transmission, Response response) throws Exception {
        if (response.getCode() == ResponseCode.OK) {
            EventBus.getDefault().post(new MyAccountInfoReceivedEvent((AccountInfo) response.getExtra()));
        } else {
            showErrorMessage("Can not fetch user info", response.getExtra());
        }
    }
}
