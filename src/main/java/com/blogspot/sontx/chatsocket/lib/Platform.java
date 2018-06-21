package com.blogspot.sontx.chatsocket.lib;

import com.blogspot.sontx.chatsocket.lib.thread.Invoker;
import com.blogspot.sontx.chatsocket.lib.view.ViewFactory;

public interface Platform {
    ViewFactory getViewFactory();
    Invoker getInvoker();
}
