package com.blogspot.sontx.chatsocket.server.model.handler;

import com.blogspot.sontx.chatsocket.server.event.RequestReceivedEvent;

public interface RequestHandler {
    void handle(RequestReceivedEvent event) throws Exception;
}
