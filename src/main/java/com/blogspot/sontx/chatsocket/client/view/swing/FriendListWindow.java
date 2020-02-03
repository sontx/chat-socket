package com.blogspot.sontx.chatsocket.client.view.swing;

import com.blogspot.sontx.chatsocket.client.view.FriendListView;
import com.blogspot.sontx.chatsocket.client.view.swing.custom.FriendCellRenderer;
import com.blogspot.sontx.chatsocket.client.view.swing.custom.FriendEntry;
import com.blogspot.sontx.chatsocket.lib.Callback;
import com.blogspot.sontx.chatsocket.lib.bean.Profile;
import com.blogspot.sontx.chatsocket.lib.bo.ImagesResource;
import com.blogspot.sontx.chatsocket.lib.view.BaseSwingWindow;
import com.blogspot.sontx.chatsocket.lib.view.ClickableJLabel;
import lombok.Setter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

class FriendListWindow extends BaseSwingWindow implements FriendListView, ActionListener {
    private JList<FriendEntry> friendList;
    private ClickableJLabel myInfoField;
    @Setter
    private Runnable myInfoButtonClickListener;
    @Setter
    private Callback<Profile> friendButtonClickListener;

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
    public void setFriendList(List<Profile> friendProfileList) {
        DefaultListModel<FriendEntry> friendEntities = getFriendEntities();
        for (Profile friend : friendProfileList) {
            friendEntities.addElement(new FriendEntry(friend));
        }
    }

    private DefaultListModel<FriendEntry> getFriendEntities() {
        ListModel<FriendEntry> friendEntities = friendList.getModel();
        if (!(friendEntities instanceof DefaultListModel)) {
            DefaultListModel<FriendEntry> defaultFriendEntries = new DefaultListModel<>();
            friendList.setModel(defaultFriendEntries);
            return defaultFriendEntries;
        }
        return (DefaultListModel<FriendEntry>) friendEntities;
    }

    @Override
    public void setMyAccountInfo(Profile profile) {
        if (profile != null) {
            myInfoField.setText(profile.getStatus());
            displayMyInfoIcon("online.png");
        } else {
            myInfoField.setText("");
            displayMyInfoIcon("offline.png");
        }
    }

    @Override
    public void updateFriend(Profile friend) {
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
    public void addNewFriend(Profile newFriendInfo) {
        DefaultListModel<FriendEntry> friendEntries = getFriendEntities();
        friendEntries.addElement(new FriendEntry(newFriendInfo));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (myInfoButtonClickListener != null)
            myInfoButtonClickListener.run();
    }

    private class ItemClickHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int where = friendList.locationToIndex(e.getPoint());
                if (where > -1) {
                    FriendEntry friendEntry = friendList.getModel().getElementAt(where);
                    Profile friend = friendEntry.getProfile();
                    if (friendButtonClickListener != null)
                        friendButtonClickListener.call(friend);
                }
            }
        }
    }
}
