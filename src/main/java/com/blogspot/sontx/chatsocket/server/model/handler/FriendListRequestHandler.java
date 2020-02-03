package com.blogspot.sontx.chatsocket.server.model.handler;

import com.blogspot.sontx.chatsocket.lib.bean.Profile;
import com.blogspot.sontx.chatsocket.lib.bean.Response;
import com.blogspot.sontx.chatsocket.server.event.RequestReceivedEvent;
import com.blogspot.sontx.chatsocket.server.model.account.AccountManager;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles friend list request from the client.
 */
class FriendListRequestHandler extends AbstractRequestHandler {
    private final AccountManager accountManager;

    FriendListRequestHandler(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Override
    Response handleWithAuthenticated(RequestReceivedEvent event) throws Exception {
        List<Profile> accounts = accountManager.getAllAccounts();

        int exceptId = event.getProfile().getAccountId();
        List<Profile> friends = accounts
                .stream()
                .filter(account -> account.getAccountId() != exceptId)
                .collect(Collectors.toList());

        return okResponse(friends, event.getRequest().getCode());
    }
}
