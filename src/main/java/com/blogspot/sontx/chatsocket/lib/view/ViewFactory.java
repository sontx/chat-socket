package com.blogspot.sontx.chatsocket.lib.view;

public interface ViewFactory {
    <T> T create(Class<T> viewType);
}
