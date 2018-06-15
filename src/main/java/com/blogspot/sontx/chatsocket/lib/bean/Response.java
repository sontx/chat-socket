package com.blogspot.sontx.chatsocket.lib.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Response implements Serializable {
    private static final long serialVersionUID = -8799356878002273414L;

    private ResponseCode code;
    private RequestCode requestCode;
    private Object extra;
}
