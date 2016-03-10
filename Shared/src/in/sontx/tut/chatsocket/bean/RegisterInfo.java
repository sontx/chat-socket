package in.sontx.tut.chatsocket.bean;

import java.io.Serializable;

public class RegisterInfo implements Serializable {
	private static final long serialVersionUID = -3586442439987010899L;
	private String username;
	private String password;
	private String displayName;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
