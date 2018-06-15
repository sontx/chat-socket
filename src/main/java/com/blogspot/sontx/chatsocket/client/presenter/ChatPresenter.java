package com.blogspot.sontx.chatsocket.client.presenter;

import com.blogspot.sontx.chatsocket.client.event.ChatMessageReceivedEvent;
import com.blogspot.sontx.chatsocket.client.event.ChatWindowClosedEvent;
import com.blogspot.sontx.chatsocket.client.event.FriendInfoChangedEvent;
import com.blogspot.sontx.chatsocket.client.event.SendChatMessageEvent;
import com.blogspot.sontx.chatsocket.client.view.ChatView;
import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.bean.ChatMessage;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ChatPresenter {
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
        EventBus.getDefault().post(new SendChatMessageEvent(chatMessage));

        chatView.appendMeMyMessage(content);
        chatView.clearInput();
    }

    private void notifyChatWindowClosed() {
        EventBus.getDefault().post(new ChatWindowClosedEvent(chatWith));
    }

    public void show() {
        EventBus.getDefault().register(this);
        chatView.setTitle(chatWith.getDisplayName());
        chatView.showWindow();
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onChatMessageReceived(ChatMessageReceivedEvent event) {
        ChatMessage chatMessage = event.getChatMessage();
        if (chatMessage.getWhoId() == chatWith.getAccountId()) {
            chatView.appendFriendMessage(chatMessage.getContent());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onFriendInfoChanged(FriendInfoChangedEvent event) {
        AccountInfo newFriendInfo = event.getNewFriendInfo();
        if (newFriendInfo.getAccountId() == chatWith.getAccountId()) {
            chatView.setTitle(newFriendInfo.getDisplayName());
            chatWith = newFriendInfo;
        }
    }
}
