package com.blogspot.sontx.chatsocket.lib.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(of = "id")
public class Profile implements Serializable {
    public static final int STATE_ONLINE = 0;
    public static final int STATE_OFFLINE = 1;
    private static final long serialVersionUID = -4193811689167914857L;
    @JsonIgnore
    private String id;

    private String displayName;
    private String status;

    @JsonIgnore
    private int state;

    @JsonIgnore
    public boolean isOnline() {
        return state == STATE_ONLINE;
    }
}
