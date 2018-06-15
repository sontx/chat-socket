package com.blogspot.sontx.chatsocket.client.model.handler;

import com.blogspot.sontx.chatsocket.client.model.ChattingManager;
import com.blogspot.sontx.chatsocket.lib.bean.RequestCode;

public class ResponseHandlerFactory {
    private final ChattingManager chattingManager;

    public ResponseHandlerFactory(ChattingManager chattingManager) {
        this.chattingManager = chattingManager;
    }

    public ResponseHandler create(RequestCode requestCode) {
        switch (requestCode) {
            case FriendList:
                return new FriendListResponseHandler();
            case AccountInfo:
                return new AccountInfoResponseHandler();
            case ChatMessage:
                return new ChatMessageResponseHandler(chattingManager);
            case Login:
                return new LoginResponseHandler();
            case UpdatePassword:
                return new PasswordResponseHandler();
            case UpdateProfile:
                return new UpdateProfileResponseHandler();
            case Register:
                return new RegisterResponseHandler();
            case FriendInfoUpdated:
                return new FriendInfoResponseHandler();
            default:
                return new DoNothingResponseHandler();
        }
    }
}
