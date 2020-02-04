package com.blogspot.sontx.chatsocket.client.presenter;

import com.blogspot.sontx.chatsocket.client.event.ChatMessageReceivedEvent;
import com.blogspot.sontx.chatsocket.client.event.ChatWindowClosedEvent;
import com.blogspot.sontx.chatsocket.client.event.FriendInfoChangedEvent;
import com.blogspot.sontx.chatsocket.client.event.SendChatMessageEvent;
import com.blogspot.sontx.chatsocket.client.view.ChatView;
import com.blogspot.sontx.chatsocket.lib.AbstractPresenter;
import com.blogspot.sontx.chatsocket.lib.bean.ChatMessage;
import com.blogspot.sontx.chatsocket.lib.bean.Profile;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang3.StringUtils;

public class ChatPresenter extends AbstractPresenter<ChatView> {
    private Profile chatWith;

    public ChatPresenter(ChatView chatView, Profile chatWith) {
        super(chatView);
        this.chatWith = chatWith;
    }

    @Override
    protected void wireUpViewEvents() {
        super.wireUpViewEvents();
        view.setSendButtonClickListener(this::sendChatMessage);
    }

    @Override
    protected void onUserClosesView() {
        notifyChatWindowClosed();
        super.onUserClosesView();
    }

    private void sendChatMessage(String content) {
        if (StringUtils.isEmpty(content)) return;

        ChatMessage chatMessage = new ChatMessage(chatWith.getId(), content);
        post(new SendChatMessageEvent(chatMessage));

        view.appendMeMyMessage(content);
        view.clearInput();
    }

    private void notifyChatWindowClosed() {
        stop();
        post(new ChatWindowClosedEvent(chatWith));
    }

    public void show() {
        start();
        view.setTitle(chatWith.getDisplayName());
        view.showWindow();
    }

    @Subscribe
    public void onChatMessageReceived(ChatMessageReceivedEvent event) {
        runOnUiThread(() -> {
            ChatMessage chatMessage = event.getChatMessage();
            if (chatMessage.getWhoId().equals(chatWith.getId())) {
                view.appendFriendMessage(chatMessage.getContent());
            }
        });
    }

    @Subscribe
    public void onFriendInfoChanged(FriendInfoChangedEvent event) {
        runOnUiThread(() -> {
            Profile newFriendInfo = event.getNewFriendInfo();
            if (newFriendInfo.getId().equals(chatWith.getId())) {
                view.setTitle(newFriendInfo.getDisplayName());
                chatWith = newFriendInfo;
            }
        });
    }
}
