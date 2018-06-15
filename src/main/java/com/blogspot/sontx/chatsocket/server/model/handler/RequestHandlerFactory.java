package com.blogspot.sontx.chatsocket.server.model.handler;

import com.blogspot.sontx.chatsocket.lib.bean.RequestCode;
import com.blogspot.sontx.chatsocket.server.model.account.AccountManager;

public class RequestHandlerFactory {
    private final AccountManager accountManager;

    public RequestHandlerFactory(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public RequestHandler create(RequestCode requestCode) {
        switch (requestCode) {
            case FriendList:
                return new FriendListRequestHandler(accountManager);
            case AccountInfo:
                return new AccountInfoRequestHandler();
            case ChatMessage:
                return new ChatMessageRequestHandler();
            case Login:
                return new LoginRequestHandler(accountManager);
            case UpdatePassword:
                return new PasswordRequestHandler(accountManager);
            case UpdateProfile:
                return new UpdateProfileRequestHandler(accountManager);
            case Register:
                return new RegisterRequestHandler(accountManager);
            default:
                return new DoNothingRequestHandler();
        }
    }
}
