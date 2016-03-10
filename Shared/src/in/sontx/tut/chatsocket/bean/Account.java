package in.sontx.tut.chatsocket.bean;

import java.io.Serializable;

public class Account implements Serializable {
	private static final long serialVersionUID = 757708636228559068L;
	private int id;
	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
