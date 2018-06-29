package com.blogspot.sontx.chatsocket.client.model.handler;

import com.blogspot.sontx.chatsocket.client.model.ChattingManager;
import com.blogspot.sontx.chatsocket.lib.bean.RequestCode;
import com.blogspot.sontx.chatsocket.lib.platform.Platform;

public class ResponseHandlerFactory {
    private final ChattingManager chattingManager;
    private final Platform platform;

    public ResponseHandlerFactory(ChattingManager chattingManager, Platform platform) {
        this.chattingManager = chattingManager;
        this.platform = platform;
    }

    public ResponseHandler create(RequestCode requestCode) {
        switch (requestCode) {
            case FriendList:
                return new FriendListResponseHandler().build(platform);
            case AccountInfo:
                return new AccountInfoResponseHandler().build(platform);
            case ChatMessage:
                return new ChatMessageResponseHandler(chattingManager).build(platform);
            case Login:
                return new LoginResponseHandler().build(platform);
            case UpdatePassword:
                return new PasswordResponseHandler().build(platform);
            case UpdateProfile:
                return new UpdateProfileResponseHandler().build(platform);
            case Register:
                return new RegisterResponseHandler().build(platform);
            case FriendInfoUpdated:
                return new FriendInfoResponseHandler().build(platform);
            default:
                return new DoNothingResponseHandler().build(platform);
        }
    }
}
