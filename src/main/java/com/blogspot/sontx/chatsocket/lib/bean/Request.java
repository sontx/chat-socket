package com.blogspot.sontx.chatsocket.lib.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request implements Serializable {
    private static final long serialVersionUID = 982079171646222501L;

    private RequestCode code;
    private Object extra;
}
