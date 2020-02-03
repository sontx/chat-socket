package com.blogspot.sontx.chatsocket.lib.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 5140826547264710686L;

    private String whoId;
    private String content;
}
