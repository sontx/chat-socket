package com.blogspot.sontx.chatsocket.client.presenter;

import com.blogspot.sontx.chatsocket.AppConfig;
import com.blogspot.sontx.chatsocket.client.event.*;
import com.blogspot.sontx.chatsocket.client.view.FriendListView;
import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.service.AbstractService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onMyAccountInfoReceived(MyAccountInfoReceivedEvent event) {
        setMyAccountInfo(event.getMyAccountInfo());
    }

    public void setMyAccountInfo(AccountInfo myAccountInfo) {
        this.myAccountInfo = myAccountInfo;
        friendListView.setMyAccountInfo(this.myAccountInfo);
        if (this.myAccountInfo != null)
            friendListView.setTitle("Homechat - Welcome " + this.myAccountInfo.getDisplayName());
        else
            friendListView.setTitle("Homechat");
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onFriendListReceived(FriendListReceivedEvent event) {
        friendList = event.getFriendList();
        friendListView.setFriendList(friendList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onFriendInfoChanged(FriendInfoChangedEvent event) {
        AccountInfo newFriendInfo = event.getNewFriendInfo();
        if (friendList != null && friendList.stream().anyMatch(friend -> friend.getAccountId() == newFriendInfo.getAccountId())) {
            friendListView.updateFriend(newFriendInfo);
        } else {
            friendListView.addNewFriend(newFriendInfo);
        }
    }
}
