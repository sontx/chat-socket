package com.blogspot.sontx.chatsocket.lib.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

	public ChatRequest(int code) {
		this.code = code;
	}
}
