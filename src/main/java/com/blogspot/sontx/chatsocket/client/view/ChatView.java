package com.blogspot.sontx.chatsocket.client.view;

import com.blogspot.sontx.chatsocket.lib.Callback;
import com.blogspot.sontx.chatsocket.lib.view.BaseView;

public interface ChatView extends BaseView {
    void appendFriendMessage(String message);

    void appendMeMyMessage(String message);

    void setSendButtonClickListener(Callback<String> listener);

    void clearInput();
}
