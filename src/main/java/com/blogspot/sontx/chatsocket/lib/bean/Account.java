package com.blogspot.sontx.chatsocket.lib.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Account implements Serializable {
	private static final long serialVersionUID = 757708636228559068L;
	private int id;
	private String username;
	private String password;
}
