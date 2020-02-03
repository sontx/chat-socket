package com.blogspot.sontx.chatsocket.server.model.handler;

import com.blogspot.sontx.chatsocket.lib.bean.Profile;
import com.blogspot.sontx.chatsocket.lib.bean.RequestCode;
import com.blogspot.sontx.chatsocket.lib.bean.Response;
import com.blogspot.sontx.chatsocket.lib.bean.ResponseCode;
import com.blogspot.sontx.chatsocket.lib.service.AbstractService;
import com.blogspot.sontx.chatsocket.server.event.AccountInfoChangedEvent;
import com.blogspot.sontx.chatsocket.server.event.RequestReceivedEvent;
import com.blogspot.sontx.chatsocket.server.model.Worker;
import org.apache.commons.lang3.NotImplementedException;

abstract class AbstractRequestHandler extends AbstractService implements RequestHandler {
    Response okResponse(Object extra, RequestCode requestCode) {
        return buildResponse(extra, ResponseCode.OK, requestCode);
    }

    Response failResponse(Object extra, RequestCode requestCode) {
        return buildResponse(extra, ResponseCode.Fail, requestCode);
    }

    private Response buildResponse(Object extra, ResponseCode code, RequestCode requestCode) {
        Response result = new Response();
        result.setCode(code);
        result.setRequestCode(requestCode);
        result.setExtra(extra);
        return result;
    }

    @Override
    public void handle(RequestReceivedEvent event) throws Exception {
        Worker sender = event.getSender();
        if (authenticate(event))
            sender.response(handleWithAuthenticated(event));
        else
            sender.response(failResponse("Unauthenticated", event.getRequest().getCode()));
    }

    Response handleWithAuthenticated(RequestReceivedEvent event) throws Exception {
        throw new NotImplementedException("handleWithAuthenticated method not implemented.");
    }

    private boolean authenticate(RequestReceivedEvent event) {
        return event.getRequest() != null && event.getProfile() != null;
    }

    void broadcastAccountInfoChanged(Profile profile) {
        post(new AccountInfoChangedEvent(profile));
    }
}
