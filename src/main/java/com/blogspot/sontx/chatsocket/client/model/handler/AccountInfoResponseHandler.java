package com.blogspot.sontx.chatsocket.client.model.handler;

import com.blogspot.sontx.chatsocket.client.event.MyAccountInfoReceivedEvent;
import com.blogspot.sontx.chatsocket.lib.bean.Profile;
import com.blogspot.sontx.chatsocket.lib.bean.Response;
import com.blogspot.sontx.chatsocket.lib.bean.ResponseCode;
import com.blogspot.sontx.chatsocket.lib.bo.ObjectTransmission;

public class AccountInfoResponseHandler extends AbstractResponseHandler {
    @Override
    public void handle(ObjectTransmission transmission, Response response) throws Exception {
        if (response.getCode() == ResponseCode.OK) {
            post(new MyAccountInfoReceivedEvent((Profile) response.getExtra()));
        } else {
            showErrorMessage("Can not fetch user info", response.getExtra());
        }
    }
}
