package com.blogspot.sontx.chatsocket.client.view;

import com.blogspot.sontx.chatsocket.lib.Callback;
import com.blogspot.sontx.chatsocket.lib.bean.Profile;
import com.blogspot.sontx.chatsocket.lib.view.BaseView;

import java.util.List;

public interface FriendListView extends BaseView {
    void setMyInfoButtonClickListener(Runnable listener);

    void setFriendButtonClickListener(Callback<Profile> listener);

    void setFriendList(List<Profile> friendProfileList);

    void setMyAccountInfo(Profile profile);

    void updateFriend(Profile friendInfo);

    void addNewFriend(Profile newFriendInfo);
}
