package com.blogspot.sontx.chatsocket.client.model;

import com.blogspot.sontx.chatsocket.client.event.ChatMessageReceivedEvent;
import com.blogspot.sontx.chatsocket.client.event.ChatWindowClosedEvent;
import com.blogspot.sontx.chatsocket.client.event.FriendListReceivedEvent;
import com.blogspot.sontx.chatsocket.client.event.OpenChatEvent;
import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.bean.ChatMessage;
import com.blogspot.sontx.chatsocket.lib.bean.Response;
import com.blogspot.sontx.chatsocket.lib.bean.ResponseCode;
import com.blogspot.sontx.chatsocket.lib.view.MessageBox;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChattingManagerImpl implements ChattingManager {
    private final List<ManagedFriend> managedFriends;
    private final Object lock = new Object();
    private boolean started;

    public ChattingManagerImpl() {
        managedFriends = new ArrayList<>();
    }

    @Override
    public void start() {
        if (!started) {
            EventBus.getDefault().register(this);
            started = true;
        }
    }

    @Override
    public void stop() {
        if (started) {
            EventBus.getDefault().unregister(this);
            started = false;
        }
    }

    @Override
    public void processReceivedChatMessage(ChatMessage chatMessage) {
        int friendId = chatMessage.getWhoId();
        if (!isChattingWith(friendId)) {
            openChatWindow(friendId);
        }
        EventBus.getDefault().post(new ChatMessageReceivedEvent(chatMessage));
    }

    @Override
    public void processSentChatMessage(Response response) {
        if (response.getCode() == ResponseCode.Fail) {
            MessageBox.showInUIThread(
                    null,
                    "Can not send chat message to your friend: " + response.getExtra(),
                    MessageBox.MESSAGE_ERROR);
        }
    }

    private boolean isChattingWith(int friendId) {
        synchronized (lock) {
            return managedFriends
                    .stream()
                    .anyMatch(friend -> friend.accountInfo.getAccountId() == friendId && friend.isChattingWith);
        }
    }

    private void openChatWindow(int friendId) {
        Optional<ManagedFriend> foundFriend = managedFriends
                .stream()
                .filter(friend -> friend.accountInfo.getAccountId() == friendId)
                .findFirst();

        foundFriend.ifPresent(this::openChatWindow);
    }

    private void openChatWindow(ManagedFriend friend) {
        EventBus.getDefault().post(new OpenChatEvent(friend.accountInfo));
        friend.isChattingWith = true;
    }

    private void updateCurrentManagedFriends(List<AccountInfo> updatedFriends) {
        for (ManagedFriend managedFriend : managedFriends) {
            for (AccountInfo updatedFriend : updatedFriends) {
                if (updatedFriend.getAccountId() == managedFriend.accountInfo.getAccountId()) {
                    managedFriend.accountInfo = updatedFriend;
                }
            }
        }
    }

    private void addNewManagedFriends(List<AccountInfo> updatedFriends) {
        List<AccountInfo> newFriends = updatedFriends
                .stream()
                .filter(updatedFriend -> managedFriends
                        .stream()
                        .noneMatch(managedFriend -> managedFriend.accountInfo.getAccountId() == updatedFriend.getAccountId()))
                .collect(Collectors.toList());

        managedFriends.addAll(newFriends
                .stream()
                .map(newFriend -> new ManagedFriend(newFriend, false))
                .collect(Collectors.toList()));
    }

    private void setChattingWithState(int friendId, boolean isChattingWith) {
        synchronized (lock) {
            managedFriends.forEach(managedFriend -> {
                if (managedFriend.accountInfo.getAccountId() == friendId) {
                    managedFriend.isChattingWith = isChattingWith;
                }
            });
        }
    }

    @Subscribe
    public void onOpenChat(OpenChatEvent event) {
        setChattingWithState(event.getChatWith().getAccountId(), true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onChatWindowClosed(ChatWindowClosedEvent event) {
        AccountInfo chatWith = event.getChatWith();
        setChattingWithState(chatWith.getAccountId(), false);
    }

    @Subscribe
    public void onFriendListReceived(FriendListReceivedEvent event) {
        List<AccountInfo> updatedFriends = event.getFriendList();
        updateCurrentManagedFriends(updatedFriends);
        addNewManagedFriends(updatedFriends);
    }

    @Data
    @AllArgsConstructor
    private static class ManagedFriend {
        private AccountInfo accountInfo;
        private boolean isChattingWith;
    }
}
