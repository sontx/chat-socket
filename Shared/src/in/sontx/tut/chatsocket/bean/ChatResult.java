package in.sontx.tut.chatsocket.bean;

import java.io.Serializable;

public class ChatResult implements Serializable {
	private static final long serialVersionUID = -8799356878002273414L;
	public static final int CODE_OK = 0;
	public static final int CODE_FAIL = 1;
	private int code;
	private int requestCode;
	private Object extra;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}

	public Object getExtra() {
		return extra;
	}

	public void setExtra(Object extra) {
		this.extra = extra;
	}
}
