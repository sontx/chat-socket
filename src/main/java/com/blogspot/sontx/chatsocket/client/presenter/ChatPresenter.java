package com.blogspot.sontx.chatsocket.client.presenter;

import com.blogspot.sontx.chatsocket.client.event.ChatMessageReceivedEvent;
import com.blogspot.sontx.chatsocket.client.event.ChatWindowClosedEvent;
import com.blogspot.sontx.chatsocket.client.event.FriendInfoChangedEvent;
import com.blogspot.sontx.chatsocket.client.event.SendChatMessageEvent;
import com.blogspot.sontx.chatsocket.client.view.ChatView;
import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.bean.ChatMessage;
import com.blogspot.sontx.chatsocket.lib.service.AbstractService;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang3.StringUtils;

public class ChatPresenter extends AbstractService implements Presenter {
    private final ChatView chatView;
    private AccountInfo chatWith;

    public ChatPresenter(ChatView chatView, AccountInfo chatWith) {
        this.chatView = chatView;
        this.chatWith = chatWith;
        wireUpViewEvents();
    }

    private void wireUpViewEvents() {
        chatView.setSendButtonClickListener(this::sendChatMessage);
        chatView.setOnClosingListener(this::notifyChatWindowClosed);
    }

    private void sendChatMessage(String content) {
        if (StringUtils.isEmpty(content)) return;

        ChatMessage chatMessage = new ChatMessage(chatWith.getAccountId(), content);
        post(new SendChatMessageEvent(chatMessage));

        chatView.appendMeMyMessage(content);
        chatView.clearInput();
    }

    private void notifyChatWindowClosed() {
        stop();
        post(new ChatWindowClosedEvent(chatWith));
    }

    public void show() {
        start();
        chatView.setTitle(chatWith.getDisplayName());
        chatView.showWindow();
    }

    @Subscribe
    public void onChatMessageReceived(ChatMessageReceivedEvent event) {
        runOnUiThread(() -> {
            ChatMessage chatMessage = event.getChatMessage();
            if (chatMessage.getWhoId() == chatWith.getAccountId()) {
                chatView.appendFriendMessage(chatMessage.getContent());
            }
        });
    }

    @Subscribe
    public void onFriendInfoChanged(FriendInfoChangedEvent event) {
        runOnUiThread(() -> {
            AccountInfo newFriendInfo = event.getNewFriendInfo();
            if (newFriendInfo.getAccountId() == chatWith.getAccountId()) {
                chatView.setTitle(newFriendInfo.getDisplayName());
                chatWith = newFriendInfo;
            }
        });
    }
}
