package com.blogspot.sontx.chatsocket.client.view;

import com.blogspot.sontx.chatsocket.client.view.custom.FriendCellRenderer;
import com.blogspot.sontx.chatsocket.client.view.custom.FriendEntry;
import com.blogspot.sontx.chatsocket.lib.Callback;
import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.bo.ImagesResource;
import com.blogspot.sontx.chatsocket.lib.view.BaseWindow;
import com.blogspot.sontx.chatsocket.lib.view.ClickableJLabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FriendListWindow extends BaseWindow implements FriendListView, ActionListener {
    private JList<FriendEntry> friendList;
    private ClickableJLabel myInfoField;
    private Runnable myInfoButtonClick;
    private Callback<AccountInfo> friendButtonClick;

    @Override
    protected void initializeComponents() {
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 5, 10, 5));
        getContentPane().add(panel, BorderLayout.NORTH);
        panel.setLayout(new BorderLayout(0, 0));

        myInfoField = new ClickableJLabel("so, do you love me?");
        myInfoField.addActionListener(this);
        panel.add(myInfoField);

        friendList = new JList<>();
        friendList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        friendList.setCellRenderer(new FriendCellRenderer());

        JScrollPane scrollPane = new JScrollPane(friendList);
        getContentPane().add(scrollPane);

        friendList.addMouseListener(new ItemClickHandler());

        setSize(300, 450);

        displayMyInfoIcon("offline.png");
    }

    private void displayMyInfoIcon(String imageName) {
        myInfoField.setIcon(new ImageIcon(ImagesResource.getInstance().getImageByName(imageName)));
    }

    @Override
    public void setMyInfoButtonClickListener(Runnable listener) {
        this.myInfoButtonClick = listener;
    }

    @Override
    public void setFriendButtonClickListener(Callback<AccountInfo> listener) {
        this.friendButtonClick = listener;
    }

    @Override
    public void setFriendList(List<AccountInfo> friendAccountInfoList) {
        DefaultListModel<FriendEntry> friendEntities = getFriendEntities();
        for (AccountInfo friend : friendAccountInfoList) {
            friendEntities.addElement(new FriendEntry(friend));
        }
    }

    private DefaultListModel<FriendEntry> getFriendEntities() {
        ListModel<FriendEntry> friendEntities = friendList.getModel();
        if (friendEntities == null || !(friendEntities instanceof DefaultListModel)) {
            DefaultListModel<FriendEntry> defaultFriendEntries = new DefaultListModel<>();
            friendList.setModel(defaultFriendEntries);
            return defaultFriendEntries;
        }
        return (DefaultListModel<FriendEntry>) friendEntities;
    }

    @Override
    public void setMyAccountInfo(AccountInfo accountInfo) {
        if (accountInfo != null) {
            myInfoField.setText(accountInfo.getStatus());
            displayMyInfoIcon("online.png");
        } else {
            myInfoField.setText("");
            displayMyInfoIcon("offline.png");
        }
    }

    @Override
    public void updateFriend(AccountInfo friend) {
        DefaultListModel<FriendEntry> friendEntities = getFriendEntities();
        int count = friendEntities.size();
        for (int i = 0; i < count; i++) {
            FriendEntry friendEntry = friendEntities.getElementAt(i);
            if (friendEntry.contains(friend)) {
                friendEntities.setElementAt(new FriendEntry(friend), i);
                break;
            }
        }
    }

    @Override
    public void addNewFriend(AccountInfo newFriendInfo) {
        DefaultListModel<FriendEntry> friendEntries = getFriendEntities();
        friendEntries.addElement(new FriendEntry(newFriendInfo));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (myInfoButtonClick != null)
            myInfoButtonClick.run();
    }

    private class ItemClickHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int where = friendList.locationToIndex(e.getPoint());
                if (where > -1) {
                    FriendEntry friendEntry = friendList.getModel().getElementAt(where);
                    AccountInfo friend = friendEntry.getAccountInfo();
                    if (friendButtonClick != null)
                        friendButtonClick.call(friend);
                }
            }
        }
    }
}
