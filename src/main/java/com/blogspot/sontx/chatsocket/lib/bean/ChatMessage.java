package com.blogspot.sontx.chatsocket.lib.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatMessage implements Serializable {
	private static final long serialVersionUID = 5140826547264710686L;

	private int whoId;
	private String content;
}
