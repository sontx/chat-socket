package com.blogspot.sontx.chatsocket.lib.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfo implements Serializable {
    private static final long serialVersionUID = 757708636228559068L;

    private String username;
    private String password;
}
