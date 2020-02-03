package com.blogspot.sontx.chatsocket.client.model;

import com.blogspot.sontx.chatsocket.client.event.*;
import com.blogspot.sontx.chatsocket.lib.bean.Profile;
import com.blogspot.sontx.chatsocket.lib.bean.ChatMessage;
import com.blogspot.sontx.chatsocket.lib.bean.Response;
import com.blogspot.sontx.chatsocket.lib.bean.ResponseCode;
import com.blogspot.sontx.chatsocket.lib.service.AbstractService;
import com.blogspot.sontx.chatsocket.lib.service.message.MessageType;
import com.google.common.eventbus.Subscribe;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChattingManagerImpl extends AbstractService implements ChattingManager {
    private final List<ManagedFriend> managedFriends;
    private final Object lock = new Object();

    public ChattingManagerImpl() {
        managedFriends = new ArrayList<>();
    }

    @Override
    public void processReceivedChatMessage(ChatMessage chatMessage) {
        String friendId = chatMessage.getWhoId();
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

    private boolean isChattingWith(String friendId) {
        synchronized (lock) {
            return managedFriends
                    .stream()
                    .anyMatch(friend -> friend.profile.getAccountId().equals(friendId) && friend.isChattingWith);
        }
    }

    private void openChatWindow(String friendId) {
        Optional<ManagedFriend> foundFriend = managedFriends
                .stream()
                .filter(friend -> friend.profile.getAccountId().equals(friendId))
                .findFirst();

        foundFriend.ifPresent(this::openChatWindow);
    }

    private void openChatWindow(ManagedFriend friend) {
        post(new OpenChatEvent(friend.profile));
        friend.isChattingWith = true;
    }

    private void updateCurrentManagedFriends(List<Profile> updatedFriends) {
        for (ManagedFriend managedFriend : managedFriends) {
            for (Profile updatedFriend : updatedFriends) {
                if (updatedFriend.getAccountId().equals(managedFriend.profile.getAccountId())) {
                    managedFriend.profile = updatedFriend;
                }
            }
        }
    }

    private void addNewManagedFriends(List<Profile> updatedFriends) {
        List<Profile> newFriends = updatedFriends
                .stream()
                .filter(updatedFriend -> managedFriends
                        .stream()
                        .noneMatch(managedFriend -> managedFriend.profile.getAccountId().equals(updatedFriend.getAccountId())))
                .collect(Collectors.toList());

        managedFriends.addAll(newFriends
                .stream()
                .map(newFriend -> new ManagedFriend(newFriend, false))
                .collect(Collectors.toList()));
    }

    private void setChattingWithState(String friendId, boolean isChattingWith) {
        synchronized (lock) {
            managedFriends.forEach(managedFriend -> {
                if (managedFriend.profile.getAccountId().equals(friendId)) {
                    managedFriend.isChattingWith = isChattingWith;
                }
            });
        }
    }

    @Subscribe
    public void onOpenChat(OpenChatEvent event) {
        setChattingWithState(event.getChatWith().getAccountId(), true);
    }

    @Subscribe
    public void onChatWindowClosed(ChatWindowClosedEvent event) {
        runOnUiThread(() -> {
            Profile chatWith = event.getChatWith();
            setChattingWithState(chatWith.getAccountId(), false);
        });
    }

    @Subscribe
    public void onFriendListReceived(FriendListReceivedEvent event) {
        List<Profile> updatedFriends = event.getFriendList();
        updateManagedFriends(updatedFriends);
    }

    private void updateManagedFriends(List<Profile> updatedFriends) {
        updateCurrentManagedFriends(updatedFriends);
        addNewManagedFriends(updatedFriends);
    }

    @Subscribe
    public void onFriendInfoChanged(FriendInfoChangedEvent event) {
        Profile newFriendInfo = event.getNewFriendInfo();
        List<Profile> updatedFriends = Collections.singletonList(newFriendInfo);
        updateManagedFriends(updatedFriends);
    }

    @Data
    @AllArgsConstructor
    private static class ManagedFriend {
        private Profile profile;
        private boolean isChattingWith;
    }
}
