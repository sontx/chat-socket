package com.blogspot.sontx.chatsocket.client.ui;

import com.blogspot.sontx.chatsocket.client.Application;
import com.blogspot.sontx.chatsocket.client.Client;
import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.bean.ChatMessage;
import com.blogspot.sontx.chatsocket.lib.bean.ChatRequest;
import com.blogspot.sontx.chatsocket.lib.bean.ChatResult;
import com.blogspot.sontx.chatsocket.lib.bo.ResourceManager;
import com.blogspot.sontx.chatsocket.lib.utils.Task;
import com.blogspot.sontx.chatsocket.lib.view.ClickableJLabel;
import com.blogspot.sontx.chatsocket.lib.view.TwoLineJLabel;
import lombok.extern.log4j.Log4j;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

@Log4j
public class FriendsWindow extends Window implements Client.OnDataReceivedListener, ActionListener {
	private static final long serialVersionUID = -849445801429980623L;
	private JList<FriendEntry> friendList;
	private ClickableJLabel myInfoField;
	private AccountInfo myAccountInfo = null;
	private Object lock = new Object();

	@Override
	protected void initializeComponents() {
		setTitle("Friends");
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 5, 10, 5));
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		myInfoField = new ClickableJLabel("so, do you love me?");
		myInfoField.setIcon(new ImageIcon(ResourceManager.getInstance().getImageByName("online.png")));
		myInfoField.addActionListener(this);
		panel.add(myInfoField);

		friendList = new JList<>();
		friendList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		friendList.setCellRenderer(new FriendCellRenderer());

		JScrollPane scrollPane = new JScrollPane(friendList);
		getContentPane().add(scrollPane);

		setSize(300, 450);
	}

	public FriendsWindow() {
		Client.getInstance().addOnDataReceivedListener(this);
		fetchDisplayData();
		friendList.addMouseListener(new ItemClickHanlder());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		((ProfileWindow) Application.showWindow(ProfileWindow.class))
				.setProfileInfo(myAccountInfo, Client.getInstance().getMyUsername());
	}

	private void displayChatBox(int whoIndex) {
		if (whoIndex >= 0) {
			AccountInfo friend = friendList.getModel().getElementAt(whoIndex).getAccountInfo();
			if (friend.getState() == AccountInfo.STATE_ONLINE)
				Application.showChatWindow(friend);
			else
				MessageBox.showMessageBoxInUIThread(this,
						String.format("'%s' has gone! wait for him online then chat again.", friend.getDisplayName()),
						MessageBox.MESSAGE_INFO);
		}
	}

	private void fetchDisplayData() {
		Task.run(() -> {
            Client.getInstance().request(new ChatRequest(ChatRequest.CODE_MY_ACCOUNT_INFO));
            Client.getInstance().request(new ChatRequest(ChatRequest.CODE_FRIENDS_LIST));
        });
	}

	@Override
	protected void onWindowClosing() {
		super.onWindowClosing();
		Client.getInstance().removeOnDataReceivedListener(this);
		Application.exitIfNotWindowActived();
	}

	private AccountInfo getAccountInfoById(int id) {
		DefaultListModel<FriendEntry> friendEntries = (DefaultListModel<FriendEntry>) friendList.getModel();
		synchronized (lock) {
			for (int i = 0; i < friendEntries.size(); i++) {
				FriendEntry friendEntry = friendEntries.getElementAt(i);
				if (friendEntry.getAccountInfo().getAccountId() == id)
					return friendEntry.getAccountInfo();
			}
		}
		return null;
	}

	private void loadFriendsList(List<AccountInfo> accountInfos) {
		DefaultListModel<FriendEntry> friendEntries = new DefaultListModel<FriendEntry>();
		for (AccountInfo accountInfo : accountInfos) {
			friendEntries.addElement(new FriendEntry(accountInfo));
		}
		friendList.setModel(friendEntries);
	}

	private void updateFriend(final AccountInfo friend) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public synchronized void run() {
				synchronized (lock) {
					FriendEntry currentEntry = null;
					int currentId = -1;
					DefaultListModel<FriendEntry> friendEntries = (DefaultListModel<FriendEntry>) friendList.getModel();
					int countOfFriend = friendEntries.getSize();
					for (int i = 0; i < countOfFriend; i++) {
						FriendEntry friendEntry = friendEntries.getElementAt(i);
						if (friendEntry.getAccountInfo().getAccountId() == friend.getAccountId()) {
							currentEntry = friendEntry;
							currentId = i;
							break;
						}
					}
					if (currentEntry != null)
						friendEntries.setElementAt(new FriendEntry(friend), currentId);
					else
						friendEntries.addElement(new FriendEntry(friend));
				}
			}
		});
	}

	private void setMyAccountInfo(final AccountInfo accountInfo) {
		myAccountInfo = accountInfo;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setTitle(accountInfo.getDisplayName());
				myInfoField.setText(accountInfo.getStatus());
				Client.getInstance().setMyId(accountInfo.getAccountId());
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onDataReceived(Client sender, ChatResult receivedObject) {
		int requestCode = receivedObject.getRequestCode();
		switch (requestCode) {
		case ChatRequest.CODE_CHAT_MESSAGE:
			if (receivedObject.getExtra() instanceof ChatMessage) {
				log.info("+ Data: chat message");
				ChatMessage chatMessage = (ChatMessage) receivedObject.getExtra();
				Application.showChatWindow(getAccountInfoById(chatMessage.getWhoId()));
			}
			break;
		case ChatRequest.CODE_FRIENDS_LIST:
			log.info("+ Data: friends list");
			loadFriendsList((List<AccountInfo>) receivedObject.getExtra());
			break;
		case ChatRequest.CODE_FRIEND_STATE:
			log.info("+ Data: update friend state");
			updateFriend((AccountInfo) receivedObject.getExtra());
			break;
		default:
			if (requestCode == ChatRequest.CODE_MY_ACCOUNT_INFO || 
				requestCode == ChatRequest.CODE_CHANGE_STATUS || 
				requestCode == ChatRequest.CODE_CHANGE_DISPNAME) {
				log.info("+ Data: update my account info");
				setMyAccountInfo((AccountInfo) receivedObject.getExtra());
			}
		}
		return true;
	}

	private class ItemClickHanlder extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				int where = friendList.locationToIndex(e.getPoint());
				displayChatBox(where);
			}
		}
	}

	private static class FriendEntry {
		private final AccountInfo accountInfo;

		public FriendEntry(AccountInfo accountInfo) {
			this.accountInfo = accountInfo;
		}

		public AccountInfo getAccountInfo() {
			return accountInfo;
		}

		public String getDisplayName() {
			return accountInfo.getDisplayName();
		}

		public ImageIcon getImage() {
			String imageid = accountInfo.isOnline() ? "online.png" : "offline.png";
			return new ImageIcon(ResourceManager.getInstance().getImageByName(imageid));
		}

		@Override
		public String toString() {
			return getDisplayName();
		}

		public String getStatus() {
			return accountInfo.getStatus();
		}
	}

	private static class FriendCellRenderer extends TwoLineJLabel implements ListCellRenderer<FriendEntry> {
		private static final long serialVersionUID = 7285154224115806852L;
		private static final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

		public FriendCellRenderer() {
			setOpaque(true);
			setIconTextGap(12);
		}

		public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, FriendEntry value,
				int index, boolean isSelected, boolean cellHasFocus) {
			setText(value.getDisplayName(), value.getStatus());
			setIcon(value.getImage());
			if (isSelected) {
				setBackground(HIGHLIGHT_COLOR);
				setForeground(Color.white);
			} else {
				setBackground(Color.white);
				setForeground(Color.black);
			}
			return this;
		}
	}
}
