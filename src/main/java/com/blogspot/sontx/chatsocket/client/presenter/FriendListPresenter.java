package com.blogspot.sontx.chatsocket.client.presenter;

import com.blogspot.sontx.chatsocket.AppConfig;
import com.blogspot.sontx.chatsocket.client.event.*;
import com.blogspot.sontx.chatsocket.client.view.FriendListView;
import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.service.AbstractService;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class FriendListPresenter extends AbstractService implements Presenter {
    private final FriendListView friendListView;
    private AccountInfo myAccountInfo;
    private List<AccountInfo> friendList;

    public FriendListPresenter(FriendListView friendListView) {
        this.friendListView = friendListView;
        wireUpViewEvents();
    }

    private void wireUpViewEvents() {
        friendListView.setMyInfoButtonClickListener(this::openMyProfile);
        friendListView.setFriendButtonClickListener(this::openChat);
        friendListView.setOnClosingListener(this::exitApp);
    }

    private void openMyProfile() {
        AccountInfo accountInfo = myAccountInfo;
        if (accountInfo != null) {
            post(new OpenMyProfileEvent(accountInfo));
        }
    }

    private void openChat(AccountInfo friend) {
        if (friend != null) {
            post(new OpenChatEvent(friend));
        }
    }

    private void exitApp() {
        stop();
        post(new AppShutdownEvent());
    }

    public void show() {
        start();
        post(new UpdateFriendListEvent());

        friendListView.setMainWindow();
        friendListView.setTitle(String.format("%s %s", AppConfig.getDefault().getAppName(), AppConfig.getDefault().getAppVersion()));
        friendListView.showWindow();
    }

    @Subscribe
    public void onMyAccountInfoReceived(MyAccountInfoReceivedEvent event) {
        runOnUiThread(() -> setMyAccountInfo(event.getMyAccountInfo()));
    }

    public void setMyAccountInfo(AccountInfo myAccountInfo) {
        this.myAccountInfo = myAccountInfo;
        friendListView.setMyAccountInfo(this.myAccountInfo);
        String appName = AppConfig.getDefault().getAppName();
        if (this.myAccountInfo != null)
            friendListView.setTitle(String.format("%s [%s]", appName, this.myAccountInfo.getDisplayName()));
        else
            friendListView.setTitle(appName);
    }

    @Subscribe
    public void onFriendListReceived(FriendListReceivedEvent event) {
        runOnUiThread(() -> {
            friendList = event.getFriendList();
            friendListView.setFriendList(friendList);
        });
    }

    @Subscribe
    public void onFriendInfoChanged(FriendInfoChangedEvent event) {
        runOnUiThread(() -> {
            AccountInfo newFriendInfo = event.getNewFriendInfo();

            if (friendList == null)
                friendList = new ArrayList<>();

            if (friendList.stream().anyMatch(friend -> friend.getAccountId() == newFriendInfo.getAccountId())) {
                friendListView.updateFriend(newFriendInfo);
            } else {
                friendList.add(newFriendInfo);
                friendListView.addNewFriend(newFriendInfo);
            }
        });
    }
}
