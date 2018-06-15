package com.blogspot.sontx.chatsocket.client.view;

import com.blogspot.sontx.chatsocket.lib.Callback;
import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.view.BaseView;

import java.util.List;

public interface FriendListView extends BaseView {
    void setMyInfoButtonClickListener(Runnable listener);

    void setFriendButtonClickListener(Callback<AccountInfo> listener);

    void setFriendList(List<AccountInfo> friendAccountInfoList);

    void setMyAccountInfo(AccountInfo accountInfo);

    void updateFriend(AccountInfo friendInfo);

    void addNewFriend(AccountInfo newFriendInfo);
}
