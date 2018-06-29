package com.blogspot.sontx.chatsocket.server.model.handler;

import com.blogspot.sontx.chatsocket.lib.bean.RequestCode;
import com.blogspot.sontx.chatsocket.lib.platform.Platform;
import com.blogspot.sontx.chatsocket.server.model.account.AccountManager;

public class RequestHandlerFactory {
    private final AccountManager accountManager;
    private final Platform platform;

    public RequestHandlerFactory(AccountManager accountManager, Platform platform) {
        this.accountManager = accountManager;
        this.platform = platform;
    }

    public RequestHandler create(RequestCode requestCode) {
        switch (requestCode) {
            case FriendList:
                return new FriendListRequestHandler(accountManager).build(platform);
            case AccountInfo:
                return new AccountInfoRequestHandler().build(platform);
            case ChatMessage:
                return new ChatMessageRequestHandler().build(platform);
            case Login:
                return new LoginRequestHandler(accountManager).build(platform);
            case UpdatePassword:
                return new PasswordRequestHandler(accountManager).build(platform);
            case UpdateProfile:
                return new UpdateProfileRequestHandler(accountManager).build(platform);
            case Register:
                return new RegisterRequestHandler(accountManager).build(platform);
            default:
                return new DoNothingRequestHandler().build(platform);
        }
    }
}
