package com.blogspot.sontx.chatsocket.lib.view;

import com.blogspot.sontx.chatsocket.lib.Function;
import com.blogspot.sontx.chatsocket.lib.NotRegisteredException;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractViewFactory implements ViewFactory {
    private final Map<Class, Function<Object>> views = new HashMap<>();

    protected void register(Class viewType, Function<Object> factory) {
        views.put(viewType, factory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> viewType) {
        if (views.containsKey(viewType))
            return (T) views.get(viewType).call();
        throw new NotRegisteredException("");
    }
}
