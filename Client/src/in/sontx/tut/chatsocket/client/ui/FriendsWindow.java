package in.sontx.tut.chatsocket.client.ui;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import in.sontx.tut.chatsocket.bean.AccountInfo;
import in.sontx.tut.chatsocket.bean.ChatMessage;
import in.sontx.tut.chatsocket.bean.ChatRequest;
import in.sontx.tut.chatsocket.bean.ChatResult;
import in.sontx.tut.chatsocket.bo.ResourceManager;
import in.sontx.tut.chatsocket.client.Application;
import in.sontx.tut.chatsocket.client.Client;
import in.sontx.tut.chatsocket.client.Client.OnDataReceivedListener;
import in.sontx.tut.chatsocket.utils.Log;
import in.sontx.tut.chatsocket.utils.Task;
import in.sontx.tut.chatsocket.view.ClickableJLabel;
import in.sontx.tut.chatsocket.view.TwoLineJLabel;

import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class FriendsWindow extends Window implements OnDataReceivedListener, ActionListener {
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

		friendList = new JList<FriendEntry>();
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
		((ProfileWindow) Application.showWindow(ProfileWindow.class)).setProfileInfo(myAccountInfo,
				Client.getInstance().getMyUsername());
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
		Task.run(new Runnable() {
			@Override
			public void run() {
				Client.getInstance().request(new ChatRequest(ChatRequest.CODE_MY_ACCOUNT_INFO));
				Client.getInstance().request(new ChatRequest(ChatRequest.CODE_FRIENDS_LIST));
			}
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
				Log.i("+ Data: chat message");
				ChatMessage chatMessage = (ChatMessage) receivedObject.getExtra();
				Application.showChatWindow(getAccountInfoById(chatMessage.getWhoId()));
			}
			break;
		case ChatRequest.CODE_FRIENDS_LIST:
			Log.i("+ Data: friends list");
			loadFriendsList((List<AccountInfo>) receivedObject.getExtra());
			break;
		case ChatRequest.CODE_FRIEND_STATE:
			Log.i("+ Data: update friend state");
			updateFriend((AccountInfo) receivedObject.getExtra());
			break;
		default:
			if (requestCode == ChatRequest.CODE_MY_ACCOUNT_INFO || 
				requestCode == ChatRequest.CODE_CHANGE_STATUS || 
				requestCode == ChatRequest.CODE_CHANGE_DISPNAME) {
				Log.i("+ Data: update my account info");
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
