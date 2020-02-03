package com.blogspot.sontx.chatsocket.server.model.handler;

import com.blogspot.sontx.chatsocket.lib.bean.Response;
import com.blogspot.sontx.chatsocket.server.event.RequestReceivedEvent;

/**
 * Handles account info request from the client.
 */
class AccountInfoRequestHandler extends AbstractRequestHandler {
    @Override
    Response handleWithAuthenticated(RequestReceivedEvent event) throws Exception {
        return okResponse(event.getProfile(), event.getRequest().getCode());
    }
}
