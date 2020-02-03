package com.blogspot.sontx.chatsocket.server.model.handler;

import com.blogspot.sontx.chatsocket.lib.bean.*;
import com.blogspot.sontx.chatsocket.server.event.LookupWorkerEvent;
import com.blogspot.sontx.chatsocket.server.event.RequestReceivedEvent;
import com.blogspot.sontx.chatsocket.server.model.Worker;
import com.blogspot.sontx.chatsocket.server.model.account.AccountManager;

/**
 * Handles login request from the client.
 */
class LoginRequestHandler extends AbstractRequestHandler {
    private final AccountManager accountManager;

    LoginRequestHandler(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Override
    public void handle(RequestReceivedEvent event) throws Exception {
        Worker sender = event.getSender();
        Request request = event.getRequest();
        if (request.getExtra() instanceof LoginInfo) {
            Response result = authenticate((LoginInfo) request.getExtra());
            if (result.getCode() == ResponseCode.OK)
                registerNewLoggedUser(sender, (Profile) result.getExtra());
            sender.response(result);
        } else {
            sender.response(failResponse("Extra must be login info", event.getRequest().getCode()));
        }
    }

    private void registerNewLoggedUser(Worker sender, Profile profile) {
        profile.setState(Profile.STATE_ONLINE);
        sender.setAccount(profile);
        broadcastAccountInfoChanged(profile);
    }

    private Response authenticate(LoginInfo loginInfo) {
        Profile profile = accountManager.findAccountByLoginInfo(loginInfo);
        if (profile == null)
            return failResponse("Wrong username or password", RequestCode.Login);

        LookupWorkerEvent event = new LookupWorkerEvent();
        event.setMatchedAccount(profile);
        post(event);

        Worker matchedWorker = event.getMatchedWorker();
        if (matchedWorker != null) {
            return failResponse("Already logged in other place", RequestCode.Login);
        } else {
            return okResponse(profile, RequestCode.Login);
        }
    }
}
