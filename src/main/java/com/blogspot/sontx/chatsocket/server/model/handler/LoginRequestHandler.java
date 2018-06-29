package com.blogspot.sontx.chatsocket.server.model.handler;

import com.blogspot.sontx.chatsocket.lib.bean.*;
import com.blogspot.sontx.chatsocket.server.event.LookupWorkerEvent;
import com.blogspot.sontx.chatsocket.server.event.RequestReceivedEvent;
import com.blogspot.sontx.chatsocket.server.model.Worker;
import com.blogspot.sontx.chatsocket.server.model.account.AccountManager;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

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
                registerNewLoggedUser(sender, (AccountInfo) result.getExtra());
            sender.response(result);
        } else {
            sender.response(failResponse("Extra must be login info", event.getRequest().getCode()));
        }
    }

    private void registerNewLoggedUser(Worker sender, AccountInfo accountInfo) {
        accountInfo.setState(AccountInfo.STATE_ONLINE);
        sender.setAccount(accountInfo);
        broadcastAccountInfoChanged(accountInfo);
    }

    private Response authenticate(LoginInfo loginInfo) {
        AccountInfo accountInfo = accountManager.findAccountByLoginInfo(loginInfo);
        if (accountInfo == null)
            return failResponse("Wrong username or password", RequestCode.Login);

        LookupWorkerEvent event = new LookupWorkerEvent();
        event.setMatchedAccount(accountInfo);
        post(event);

        Worker matchedWorker = event.getMatchedWorker();
        if (matchedWorker != null) {
            return failResponse("Already logged in other place", RequestCode.Login);
        } else {
            return okResponse(accountInfo, RequestCode.Login);
        }
    }
}
