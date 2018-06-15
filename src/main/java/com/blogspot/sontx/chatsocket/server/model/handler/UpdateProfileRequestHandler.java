package com.blogspot.sontx.chatsocket.server.model.handler;

import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.bean.Request;
import com.blogspot.sontx.chatsocket.lib.bean.Response;
import com.blogspot.sontx.chatsocket.lib.utils.Security;
import com.blogspot.sontx.chatsocket.server.event.RequestReceivedEvent;
import com.blogspot.sontx.chatsocket.server.model.account.AccountManager;

/**
 * Handles updating user's profile request from the client.
 */
class UpdateProfileRequestHandler extends AbstractRequestHandler {
    private final AccountManager accountManager;

    UpdateProfileRequestHandler(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Override
    Response handleWithAuthenticated(RequestReceivedEvent event) throws Exception {
        Request request = event.getRequest();
        if (request.getExtra() != null && request.getExtra() instanceof AccountInfo) {
            AccountInfo updateFrom = (AccountInfo) request.getExtra();
            if (Security.checkValidDisplayName(updateFrom.getDisplayName())) {
                AccountInfo updateTo = event.getAccountInfo();
                updateTo.setDisplayName(updateFrom.getDisplayName());
                updateTo.setStatus(updateFrom.getStatus());

                accountManager.updateDetail(updateTo);
                broadcastAccountInfoChanged(updateTo);
                return okResponse(updateTo, event.getRequest().getCode());
            }
        }
        return failResponse("Invalid display name", event.getRequest().getCode());
    }
}
