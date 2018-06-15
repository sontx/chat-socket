package com.blogspot.sontx.chatsocket.server.model.handler;

import com.blogspot.sontx.chatsocket.lib.bean.Request;
import com.blogspot.sontx.chatsocket.lib.bean.Response;
import com.blogspot.sontx.chatsocket.lib.utils.Security;
import com.blogspot.sontx.chatsocket.server.event.RequestReceivedEvent;
import com.blogspot.sontx.chatsocket.server.model.account.AccountManager;

/**
 * Handles updating password request from the client.
 */
class PasswordRequestHandler extends AbstractRequestHandler {
    private final AccountManager accountManager;

    PasswordRequestHandler(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Override
    Response handleWithAuthenticated(RequestReceivedEvent event) throws Exception {
        Request request = event.getRequest();
        if (request.getExtra() != null && Security.checkValidPassword(request.getExtra().toString())) {
            String password = request.getExtra().toString();
            String passwordHash = Security.getPasswordHash(password);
            accountManager.setPasswordHash(event.getAccountInfo().getAccountId(), passwordHash);
            return okResponse(null, event.getRequest().getCode());
        }
        return failResponse("Invalid password.", event.getRequest().getCode());
    }
}
