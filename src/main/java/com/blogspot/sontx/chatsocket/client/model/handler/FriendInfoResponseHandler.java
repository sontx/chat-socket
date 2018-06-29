package com.blogspot.sontx.chatsocket.client.model.handler;

import com.blogspot.sontx.chatsocket.client.event.FriendInfoChangedEvent;
import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.bean.Response;
import com.blogspot.sontx.chatsocket.lib.bean.ResponseCode;
import com.blogspot.sontx.chatsocket.lib.bo.ObjectTransmission;

public class FriendInfoResponseHandler extends AbstractResponseHandler {
    @Override
    public void handle(ObjectTransmission transmission, Response response) throws Exception {
        if (response.getCode() == ResponseCode.OK) {
            post(new FriendInfoChangedEvent((AccountInfo) response.getExtra()));
        }
    }
}
