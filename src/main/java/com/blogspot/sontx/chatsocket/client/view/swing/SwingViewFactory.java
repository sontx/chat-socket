package com.blogspot.sontx.chatsocket.client.view.swing;

import com.blogspot.sontx.chatsocket.client.view.*;
import com.blogspot.sontx.chatsocket.lib.view.AbstractViewFactory;

public class SwingViewFactory extends AbstractViewFactory {
    public SwingViewFactory() {
        register(ChatView.class, ChatWindow::new);
        register(ConnectionView.class, ConnectionWindow::new);
        register(FriendListView.class, FriendListWindow::new);
        register(LoginView.class, LoginWindow::new);
        register(ProfileView.class, ProfileWindow::new);
        register(RegisterView.class, RegisterWindow::new);
    }
}
