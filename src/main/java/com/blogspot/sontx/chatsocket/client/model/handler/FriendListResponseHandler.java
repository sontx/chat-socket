package com.blogspot.sontx.chatsocket.client.model.handler;

import com.blogspot.sontx.chatsocket.client.event.FriendListReceivedEvent;
import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.bean.Response;
import com.blogspot.sontx.chatsocket.lib.bean.ResponseCode;
import com.blogspot.sontx.chatsocket.lib.bo.ObjectTransmission;

import java.util.List;

public class FriendListResponseHandler extends AbstractResponseHandler {
    @Override
    @SuppressWarnings("unchecked")
    public void handle(ObjectTransmission transmission, Response response) throws Exception {
        if (response.getCode() == ResponseCode.OK) {
            post(new FriendListReceivedEvent((List<AccountInfo>) response.getExtra()));
        } else {
            showErrorMessage("Can not fetch friend list", response.getExtra());
        }
    }
}
