package com.blogspot.sontx.chatsocket.client.view.javafx;

import com.blogspot.sontx.chatsocket.client.view.*;
import com.blogspot.sontx.chatsocket.lib.view.AbstractViewFactory;

public class JavaFxViewFactory extends AbstractViewFactory {

    public JavaFxViewFactory() {
        register(ChatView.class, ChatWindow::new);
        register(ConnectionView.class, ConnectionWindow::new);
        register(FriendListView.class, FriendListWindow::new);
        register(LoginView.class, LoginWindow::new);
        register(ProfileView.class, ProfileWindow::new);
        register(RegisterView.class, RegisterWindow::new);
    }
}
