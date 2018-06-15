package com.blogspot.sontx.chatsocket.lib.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterInfo implements Serializable {
    private static final long serialVersionUID = -3586442439987010899L;

    private String username;
    private String password;
    private String displayName;
}
