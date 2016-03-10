package in.sontx.tut.chatsocket.server;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import in.sontx.tut.chatsocket.server.ui.Homechat;

public final class Program {
	private static void setSystemLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
		}
	}

	public static void main(String[] args) {
		setSystemLookAndFeel();
		new Homechat().setVisible(true);
	}
}
