package in.sontx.tut.chatsocket.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import com.sun.istack.internal.NotNull;

import in.sontx.tut.chatsocket.bean.Account;
import in.sontx.tut.chatsocket.bean.AccountInfo;
import in.sontx.tut.chatsocket.bean.ChatMessage;
import in.sontx.tut.chatsocket.bean.ChatRequest;
import in.sontx.tut.chatsocket.bean.ChatResult;
import in.sontx.tut.chatsocket.bean.RegisterInfo;
import in.sontx.tut.chatsocket.server.Worker.OnAuthenticatedListener;
import in.sontx.tut.chatsocket.server.Worker.OnRequestReceivedListener;
import in.sontx.tut.chatsocket.utils.Log;
import in.sontx.tut.chatsocket.utils.Security;
import in.sontx.tut.chatsocket.utils.StreamUtilities;
import in.sontx.tut.chatsocket.utils.Task;

public final class Server implements Closeable, OnAuthenticatedListener {
	private ServerSocket serverSocket;
	private List<Worker> workers = new LinkedList<Worker>();
	private Object lock = new Object();
	private RequestHandler requestHandler = new RequestHandler();

	public Server(int port, String address, int maxConnection) throws IOException {
		Log.i("Server starting...");
		serverSocket = new ServerSocket(port, maxConnection, InetAddress.getByName(address));
		Log.i("Server started...");
	}

	public void waitForConnection() throws IOException {
		Log.i("Waiting for connecting...");
		Socket socket = null;
		while ((socket = serverSocket.accept()) != null) {
			Log.i("Accepted " + socket.getRemoteSocketAddress().toString());
			talkWithClient(socket);
		}
	}

	private Worker createWorker(Socket socket) {
		Worker worker = null;
		try {
			Log.i("Creating worker...");
			worker = new Worker(socket);
		} catch (IOException | SecurityException e) {
			Log.i("Creating was aborted!");
		}

		if (worker != null) {
			synchronized (lock) {
				workers.add(worker);
			}
		} else {
			StreamUtilities.tryCloseStream(socket);
		}
		return worker;
	}

	private void notifyAllWorker(Worker broadcastWorker) {
		synchronized (lock) {
			ChatResult result = new ChatResult();
			result.setCode(ChatResult.CODE_OK);
			result.setRequestCode(ChatRequest.CODE_FRIEND_STATE);
			result.setExtra(broadcastWorker.getAccount());
			for (int i = 0; i < workers.size(); i++) {
				Worker friendWorker = workers.get(i);
				if (!broadcastWorker.equals(friendWorker)) {
					try {
						friendWorker.response(result);
					} catch (IOException e) {
						killWorker(friendWorker);
					}
				}
			}
		}
	}

	private void killWorker(Worker worker) {
		worker.release();
		worker.setOnReceivedDataListener(null);
		worker.setOnAuthenticatedListener(null);
		synchronized (lock) {
			workers.remove(worker);
		}
		if (worker.getAccount() != null) {
			Log.i("Removed worker " + worker.getAccount().getAccountId());
			worker.getAccount().setState(AccountInfo.STATE_OFFLINE);
			notifyAllWorker(worker);
		} else {
			Log.i("Removed anonymous worker");
		}
	}

	private void talkWithClient(final Socket socket) {
		Task.run(new Runnable() {
			@Override
			public void run() {
				Worker worker = createWorker(socket);
				if (worker != null) {
					try {
						Log.i("Start worker bridge!");
						worker.setOnAuthenticatedListener(Server.this);
						worker.setOnReceivedDataListener(requestHandler);
						worker.startBridge();
					} catch (IOException e) {
					}
					killWorker(worker);
				} else {
					Log.i("Createing worker: unsuccessful");
				}
			}
		});
	}

	@Override
	public void close() throws IOException {
		serverSocket.close();
		while (!workers.isEmpty()) {
			Worker worker = workers.get(0);
			killWorker(worker);
		}
	}

	@Override
	public void onAuthenticated(Worker worker) {
		Log.i("Broadcast new worker!");
		notifyAllWorker(worker);
	}

	private class RequestHandler implements OnRequestReceivedListener {

		@Override
		@NotNull
		public ChatResult onRequestReceived(Worker sender, ChatRequest request) {
			ChatResult responseObject = null;
			switch (request.getCode()) {
			case ChatRequest.CODE_FRIENDS_LIST:
				responseObject = responseFriendsList(sender);
				break;
			case ChatRequest.CODE_MY_ACCOUNT_INFO:
				responseObject = responseAccountInfo(sender);
				break;
			case ChatRequest.CODE_CHAT_MESSAGE:
				responseObject = forwardChatMessage(sender,
						request.getExtra() instanceof ChatMessage ? (ChatMessage) request.getExtra() : null);
				break;
			case ChatRequest.CODE_LOGIN:
				responseObject = responseLoginResult(sender,
						request.getExtra() instanceof Account ? (Account) request.getExtra() : null);
				break;
			case ChatRequest.CODE_CHANGE_DISPNAME:
				responseObject = responseChangeDisplayName(sender.getAccount(),
						request.getExtra() != null ? request.getExtra().toString() : null);
				notifyAllWorker(sender);
				break;
			case ChatRequest.CODE_CHANGE_PASSWORD:
				responseObject = responseChangePassword(sender.getAccount().getAccountId(),
						request.getExtra() != null ? request.getExtra().toString() : null);
				break;
			case ChatRequest.CODE_CHANGE_STATUS:
				responseObject = responseChangeStatus(sender.getAccount(),
						request.getExtra() != null ? request.getExtra().toString() : null);
				notifyAllWorker(sender);
				break;
			case ChatRequest.CODE_REGISTER:
				if (request.getExtra() instanceof RegisterInfo) {
					RegisterInfo registerInfo = (RegisterInfo) request.getExtra();
					responseObject = responseRegister(registerInfo.getUsername(), registerInfo.getPassword(),
							registerInfo.getDisplayName());
				}
				break;
			}
			responseObject.setRequestCode(request.getCode());
			return responseObject;
		}

		private boolean isLogged(AccountInfo accountInfo) {
			if (accountInfo == null)
				return false;
			synchronized (lock) {
				for (Worker worker : workers) {
					AccountInfo existsAccountInfo = worker.getAccount();
					if (existsAccountInfo != null && existsAccountInfo.getAccountId() == accountInfo.getAccountId()) {
						return true;
					}
				}
				return false;
			}
		}

		private ChatResult responseLoginResult(Worker sender, Account account) {
			if (account == null)
				return null;
			AccountInfo accountInfo = AccountManager.getInstance().getAccountInfo(account.getUsername(),
					Security.getPasswordHash(account.getPassword()));
			ChatResult result = new ChatResult();
			boolean logged = isLogged(accountInfo);
			result.setCode(ChatResult.CODE_FAIL);
			if (accountInfo != null) {
				if (logged) {
					result.setExtra("already logged in other place");
					Log.i("Response login: FAIL - already logged in other place");
				} else {
					result.setCode(ChatResult.CODE_OK);
					sender.setAccount(accountInfo);
					Log.i("Response login: OK");
				}
			} else {
				result.setExtra("Wrong username or password!");
				Log.i("Response login: FAIL - wrong username or password");
			}
			return result;
		}

		private ChatResult responseAccountInfo(Worker sender) {
			Log.i("Request account info for " + sender.getAccount().getAccountId());
			ChatResult result = new ChatResult();
			result.setCode(ChatResult.CODE_OK);
			result.setExtra(sender.getAccount());
			return result;
		}

		private ChatResult responseFriendsList(Worker sender) {
			Log.i("Request all friends for " + sender.getAccount().getAccountId());
			List<AccountInfo> allFriends = AccountManager.getInstance().getAllAccountInfos();
			int exceptId = sender.getAccount().getAccountId();
			AccountInfo exceptAccount = null;
			for (AccountInfo friend : allFriends) {
				if (friend.getAccountId() == exceptId) {
					exceptAccount = friend;
					continue;
				}
				for (Worker worker : workers) {
					if (worker.getAccount().equals(friend)) {
						friend.setState(AccountInfo.STATE_ONLINE);
					}
				}
			}
			allFriends.remove(exceptAccount);
			ChatResult result = new ChatResult();
			result.setCode(ChatResult.CODE_OK);
			result.setExtra(allFriends);
			return result;
		}

		private ChatResult forwardChatMessage(Worker sender, ChatMessage chatMessage) {
			if (chatMessage == null)
				return null;
			Log.l(String.format("%d > %d: %s", sender.getAccount().getAccountId(), chatMessage.getWhoId(),
					chatMessage.getContent()));

			int whoReceiverId = chatMessage.getWhoId();
			Worker whoReceiver = null;

			chatMessage.setWhoId(sender.getAccount().getAccountId());

			synchronized (lock) {
				for (Worker _receiver : workers) {
					if (_receiver.getAccount().getAccountId() == whoReceiverId) {
						whoReceiver = _receiver;
						break;
					}
				}
			}

			ChatResult result = new ChatResult();
			result.setCode(ChatResult.CODE_FAIL);
			if (whoReceiver != null) {
				try {
					ChatResult forwardResult = new ChatResult();
					forwardResult.setCode(ChatResult.CODE_OK);
					forwardResult.setRequestCode(ChatRequest.CODE_CHAT_MESSAGE);
					forwardResult.setExtra(chatMessage);
					whoReceiver.response(forwardResult);
					result.setCode(ChatResult.CODE_OK);
				} catch (IOException e) {
					// e.printStackTrace();
					result.setExtra("Friend's connection broken down!");
				}
			} else {
				result.setExtra("Friend was offline!");
			}
			return result;
		}

		private ChatResult responseRegister(String username, String password, String dispname) {
			Log.i(String.format("Register: %s", username));
			ChatResult result = new ChatResult();
			result.setCode(ChatResult.CODE_FAIL);
			if (Security.checkValidUsername(username) && Security.checkValidPassword(password)
					&& Security.checkValidDisplayName(dispname)) {
				username = username.trim();
				String passhash = Security.getPasswordHash(password);
				dispname = dispname.trim();
				AccountManager.getInstance().addAccount(username, passhash, dispname);
				result.setCode(ChatResult.CODE_OK);
			}
			return result;
		}

		private ChatResult responseChangeDisplayName(AccountInfo account, String dispname) {
			Log.i(String.format("Change display name %d to %s", account.getAccountId(), dispname));
			ChatResult result = new ChatResult();
			result.setCode(ChatResult.CODE_FAIL);
			result.setExtra(account);
			if (Security.checkValidDisplayName(dispname)) {
				dispname = dispname.trim();
				account.setDisplayName(dispname);
				AccountManager.getInstance().changeDisplayName(account.getAccountId(), dispname);
				result.setCode(ChatResult.CODE_OK);
			}
			return result;
		}

		private ChatResult responseChangeStatus(AccountInfo account, String status) {
			Log.i(String.format("Change status %d to %s", account.getAccountId(), status));
			ChatResult result = new ChatResult();
			result.setCode(ChatResult.CODE_FAIL);
			result.setExtra(account);
			if (status != null && (status = status.trim()).length() > 0) {
				account.setStatus(status);
				AccountManager.getInstance().changeStatus(account.getAccountId(), status);
				result.setCode(ChatResult.CODE_OK);
			}
			return result;
		}

		private ChatResult responseChangePassword(int id, String password) {
			Log.i(String.format("Change password %d", id));
			ChatResult result = new ChatResult();
			result.setCode(ChatResult.CODE_FAIL);
			if (Security.checkValidPassword(password)) {
				AccountManager.getInstance().changePasswordHash(id, Security.getPasswordHash(password));
				result.setCode(ChatResult.CODE_OK);
			}
			return result;
		}
	}
}
