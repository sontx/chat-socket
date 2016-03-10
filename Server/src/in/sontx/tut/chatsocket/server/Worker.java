package in.sontx.tut.chatsocket.server;

import java.io.IOException;
import java.net.Socket;

import com.sun.istack.internal.NotNull;

import in.sontx.tut.chatsocket.bean.AccountInfo;
import in.sontx.tut.chatsocket.bean.ChatRequest;
import in.sontx.tut.chatsocket.bean.ChatResult;
import in.sontx.tut.chatsocket.bo.ITransmission;
import in.sontx.tut.chatsocket.bo.ObjectAdapter;
import in.sontx.tut.chatsocket.bo.Protocol;
import in.sontx.tut.chatsocket.bo.SocketTransmission;
import in.sontx.tut.chatsocket.utils.StreamUtilities;

public class Worker {
	private AccountInfo myAccount = null;
	private ITransmission transmission;
	private ObjectAdapter objectAdapter;
	private Protocol protocol;
	private OnRequestReceivedListener mOnRequestReceivedListener = null;
	private OnAuthenticatedListener mOnAuthenticatedListener = null;

	public void setOnReceivedDataListener(OnRequestReceivedListener listener) {
		mOnRequestReceivedListener = listener;
	}
	
	public void setOnAuthenticatedListener(OnAuthenticatedListener listener) {
		mOnAuthenticatedListener = listener;
	}

	public void response(@NotNull ChatResult result) throws IOException {
		protocol.sendObject(result);
	}

	public void startBridge() throws IOException {
		while (true) {
			Object receivedObject = protocol.receiveObject();
			if (receivedObject == null)
				break;
			if (receivedObject instanceof ChatRequest) {
				if (mOnRequestReceivedListener != null)
					response(mOnRequestReceivedListener.onRequestReceived(this, (ChatRequest) receivedObject));
			}
		}
	}

	public void release() {
		StreamUtilities.tryCloseStream(transmission);
	}

	public Worker(Socket socket) throws IOException {
		transmission = new SocketTransmission(socket);
		objectAdapter = new ObjectAdapter();
		protocol = new Protocol(objectAdapter, transmission);
	}
	
	public void setAccount(AccountInfo accountInfo) {
		this.myAccount = accountInfo;
		if (accountInfo != null && mOnAuthenticatedListener != null)
			mOnAuthenticatedListener.onAuthenticated(this);
	}

	public AccountInfo getAccount() {
		return myAccount;
	}

	public interface OnAuthenticatedListener {
		void onAuthenticated(Worker worker);
	}
	
	public interface OnRequestReceivedListener {
		@NotNull
		ChatResult onRequestReceived(Worker sender, ChatRequest request);
	}
}
