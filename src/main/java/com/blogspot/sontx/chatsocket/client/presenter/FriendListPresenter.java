package com.blogspot.sontx.chatsocket.client.presenter;

import com.blogspot.sontx.chatsocket.AppConfig;
import com.blogspot.sontx.chatsocket.client.event.*;
import com.blogspot.sontx.chatsocket.client.view.FriendListView;
import com.blogspot.sontx.chatsocket.lib.AbstractPresenter;
import com.blogspot.sontx.chatsocket.lib.bean.Profile;
import com.blogspot.sontx.chatsocket.lib.event.AppShutdownEvent;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class FriendListPresenter extends AbstractPresenter<FriendListView> {
    private Profile myProfile;
    private List<Profile> friendList;

    public FriendListPresenter(FriendListView friendListView) {
        super(friendListView);
    }

    @Override
    protected  void wireUpViewEvents() {
        super.wireUpViewEvents();
        view.setMyInfoButtonClickListener(this::openMyProfile);
        view.setFriendButtonClickListener(this::openChat);
    }

    @Override
    protected void onUserClosesView() {
        exitApp();
        super.onUserClosesView();
    }

    private void openMyProfile() {
        Profile profile = myProfile;
        if (profile != null) {
            post(new OpenMyProfileEvent(profile));
        }
    }

    private void openChat(Profile friend) {
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

        view.setMainWindow();
        view.setTitle(String.format("%s %s", AppConfig.getDefault().getAppName(), AppConfig.getDefault().getAppVersion()));
        view.showWindow();
    }

    @Subscribe
    public void onMyAccountInfoReceived(MyAccountInfoReceivedEvent event) {
        runOnUiThread(() -> setMyProfile(event.getMyProfile()));
    }

    public void setMyProfile(Profile myProfile) {
        this.myProfile = myProfile;
        view.setMyAccountInfo(this.myProfile);
        String appName = AppConfig.getDefault().getAppName();
        if (this.myProfile != null)
            view.setTitle(String.format("%s [%s]", appName, this.myProfile.getDisplayName()));
        else
            view.setTitle(appName);
    }

    @Subscribe
    public void onFriendListReceived(FriendListReceivedEvent event) {
        runOnUiThread(() -> {
            friendList = event.getFriendList();
            view.setFriendList(friendList);
        });
    }

    @Subscribe
    public void onFriendInfoChanged(FriendInfoChangedEvent event) {
        runOnUiThread(() -> {
            Profile newFriendInfo = event.getNewFriendInfo();

            if (friendList == null)
                friendList = new ArrayList<>();

            if (friendList.stream().anyMatch(friend -> friend.getId() == newFriendInfo.getId())) {
                view.updateFriend(newFriendInfo);
            } else {
                friendList.add(newFriendInfo);
                view.addNewFriend(newFriendInfo);
            }
        });
    }
}
