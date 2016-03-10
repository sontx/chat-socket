package in.sontx.tut.chatsocket.bean;

import java.io.Serializable;

public class ChatMessage implements Serializable {
	private static final long serialVersionUID = 5140826547264710686L;
	private int whoId;
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getWhoId() {
		return whoId;
	}

	public void setWhoId(int whoId) {
		this.whoId = whoId;
	}
}
