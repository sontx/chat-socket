package com.blogspot.sontx.chatsocket.server.model.handler;

import com.blogspot.sontx.chatsocket.server.event.RequestReceivedEvent;

public class DoNothingRequestHandler extends AbstractRequestHandler {
    @Override
    public void handle(RequestReceivedEvent event) throws Exception {
    }
}
