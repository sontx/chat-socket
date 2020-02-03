package com.blogspot.sontx.chatsocket.client.event;

import com.blogspot.sontx.chatsocket.lib.bean.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendListReceivedEvent {
    private List<Profile> friendList;
}
