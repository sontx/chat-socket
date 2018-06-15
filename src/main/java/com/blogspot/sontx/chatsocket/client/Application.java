package com.blogspot.sontx.chatsocket.client;

import com.blogspot.sontx.chatsocket.client.ui.ChatWindow;
import com.blogspot.sontx.chatsocket.client.ui.ConnectionWindow;
import com.blogspot.sontx.chatsocket.client.ui.MessageBox;
import com.blogspot.sontx.chatsocket.client.ui.Window;
import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.bo.ResourceManager;
import com.sun.istack.internal.NotNull;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public final class Application {
	private static List<Window> windows = new LinkedList<>();

	public static void exitIfNotWindowActived() {
		if (windows.isEmpty())
			exit();
	}

	public static void exit() {
		for (int i = 0; i < windows.size(); i++) {
			Window window = windows.get(i);
			window.dispose();
		}
		Client.destroyInstance();
		ResourceManager.destroyInstance();
		System.exit(0);
	}

	public static void registerWindow(Window window) {
		windows.add(window);
	}

	public static void unregisterWindow(Window window) {
		windows.remove(window);
	}

	public static void setSystemLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
		}
	}

	public static void run() {
		((ConnectionWindow) Application.showWindow(ConnectionWindow.class))
				.setOnCreatedClientListener(new ConnectionWindowAdapter());
	}

	public static Window showWindow(Class<? extends Window> clazz) {
		Window existsWindow = null;
		for (Window window : windows) {
			if (window.getClass().equals(clazz)) {
				existsWindow = window;
				break;
			}
		}
		if (existsWindow == null) {
			try {
				existsWindow = clazz.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (existsWindow != null) {
				existsWindow.setLocationRelativeTo(null);
				existsWindow.setVisible(true);
			}
		}
		if (existsWindow != null)
			existsWindow.toFront();
		return existsWindow;
	}

	public static ChatWindow showChatWindow(@NotNull AccountInfo who) {
		ChatWindow chatWindow = null;
		for (Window window : windows) {
			if (window instanceof ChatWindow) {
				chatWindow = (ChatWindow) window;
				if (chatWindow.getAccountId() == who.getAccountId()) {
					chatWindow.requestFocus();
					return chatWindow;
				}
			}
		}
		chatWindow = new ChatWindow(who);
		chatWindow.setLocationRelativeTo(null);
		chatWindow.setVisible(true);
		return chatWindow;
	}

	public static void closeAllWindows() {
		while (windows.size() > 0) {
			Window window = windows.get(0);
			window.dispose();
		}
	}

	private static class ConnectionWindowAdapter implements ConnectionWindow.OnCreatedClientListener {
		@Override
		public void onCreatedClient(Client client) {
			client.setOnConnectionHasProblemListener(new Client.OnConnectionHasProblemListener() {
				@Override
				public void onConnectionHasProblem(String message) {
					closeAllWindows();
					MessageBox.showMessageBoxInUIThread(null, "Connection has problem: " + message, MessageBox.MESSAGE_ERROR);
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							((ConnectionWindow) Application.showWindow(ConnectionWindow.class))
									.setOnCreatedClientListener(new ConnectionWindowAdapter());
						}
					});

				}
			});
			client.startLooper();
		}
	}
}
