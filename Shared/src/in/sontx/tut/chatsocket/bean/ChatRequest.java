package in.sontx.tut.chatsocket.bean;

import java.io.Serializable;

public class ChatRequest implements Serializable {
	private static final long serialVersionUID = 982079171646222501L;
	public static final int CODE_MY_ACCOUNT_INFO = 0;
	public static final int CODE_FRIENDS_LIST = 1;
	public static final int CODE_CHAT_MESSAGE = 2;
	public static final int CODE_LOGIN = 3;
	public static final int CODE_REGISTER = 4;
	public static final int CODE_CHANGE_DISPNAME = 5;
	public static final int CODE_CHANGE_PASSWORD = 6;
	public static final int CODE_CHANGE_STATUS = 7;
	public static final int CODE_FRIEND_STATE = 8;

	private int code;
	private Object extra;

	public ChatRequest() {
	}

	public ChatRequest(int code) {
		this.code = code;
	}

	public ChatRequest(int code, Object extra) {
		this.code = code;
		this.extra = extra;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getExtra() {
		return extra;
	}

	public void setExtra(Object extra) {
		this.extra = extra;
	}
}
