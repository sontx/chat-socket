package com.blogspot.sontx.chatsocket.client.model;

import com.blogspot.sontx.chatsocket.client.event.*;
import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.bean.ChatMessage;
import com.blogspot.sontx.chatsocket.lib.bean.Response;
import com.blogspot.sontx.chatsocket.lib.bean.ResponseCode;
import com.blogspot.sontx.chatsocket.lib.service.AbstractService;
import com.blogspot.sontx.chatsocket.lib.service.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.*;
import java.util.stream.Collectors;

public class ChattingManagerImpl extends AbstractService implements ChattingManager {
    private final List<ManagedFriend> managedFriends;
    private final Object lock = new Object();

    public ChattingManagerImpl() {
        managedFriends = new ArrayList<>();
    }

    @Override
    public void processReceivedChatMessage(ChatMessage chatMessage) {
        int friendId = chatMessage.getWhoId();
        if (!isChattingWith(friendId)) {
            openChatWindow(friendId);
        }
        post(new ChatMessageReceivedEvent(chatMessage));
    }

    @Override
    public void processSentChatMessage(Response response) {
        if (response.getCode() == ResponseCode.Fail) {
            postMessageBox(
                    "Can not send the chat message to your friend: " + response.getExtra(),
                    MessageType.Error);
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
        post(new OpenChatEvent(friend.accountInfo));
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
        updateManagedFriends(updatedFriends);
    }

    private void updateManagedFriends(List<AccountInfo> updatedFriends) {
        updateCurrentManagedFriends(updatedFriends);
        addNewManagedFriends(updatedFriends);
    }

    @Subscribe
    public void onFriendInfoChanged(FriendInfoChangedEvent event) {
        AccountInfo newFriendInfo = event.getNewFriendInfo();
        List<AccountInfo> updatedFriends = Collections.singletonList(newFriendInfo);
        updateManagedFriends(updatedFriends);
    }

    @Data
    @AllArgsConstructor
    private static class ManagedFriend {
        private AccountInfo accountInfo;
        private boolean isChattingWith;
    }
}
