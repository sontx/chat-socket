package com.blogspot.sontx.chatsocket.lib.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatResult implements Serializable {
	private static final long serialVersionUID = -8799356878002273414L;
	public static final int CODE_OK = 0;
	public static final int CODE_FAIL = 1;
	private int code;
	private int requestCode;
	private Object extra;
}
