package com.blogspot.sontx.chatsocket.lib.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(of = "accountId")
public class AccountInfo implements Serializable {
	private static final long serialVersionUID = -4193811689167914857L;

	public static final int STATE_ONLINE = 0;
	public static final int STATE_OFFLINE = 1;

	private int accountId;
	private String displayName;
	private String status;
	private int state;

	public boolean isOnline() {
		return state == STATE_ONLINE;
	}
}
