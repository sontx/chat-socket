package com.blogspot.sontx.chatsocket.server.model;

import com.blogspot.sontx.chatsocket.lib.bean.Request;
import com.blogspot.sontx.chatsocket.lib.service.AbstractService;
import com.blogspot.sontx.chatsocket.server.event.RequestReceivedEvent;
import com.blogspot.sontx.chatsocket.server.model.handler.RequestHandler;
import com.blogspot.sontx.chatsocket.server.model.handler.RequestHandlerFactory;
import com.google.common.eventbus.Subscribe;
import lombok.extern.log4j.Log4j;

/**
 * Routes incomming requests from clients to handlers.
 */
@Log4j
public class RequestRouter extends AbstractService {
    private final RequestHandlerFactory requestHandlerFactory;

    public RequestRouter(RequestHandlerFactory requestHandlerFactory) {
        this.requestHandlerFactory = requestHandlerFactory;
    }

    @Subscribe
    public void onRequestReceived(RequestReceivedEvent event) {
        runAsync(() -> {
            Request request = event.getRequest();
            RequestHandler handler = requestHandlerFactory.create(request.getCode());
            try {
                handler.handle(event);
            } catch (Exception e) {
                log.error("Handle request", e);
            }
        });
    }
}
