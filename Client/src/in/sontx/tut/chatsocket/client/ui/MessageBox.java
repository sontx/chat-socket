package in.sontx.tut.chatsocket.client.ui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import in.sontx.tut.chatsocket.bo.ResourceManager;

public final class MessageBox {
	public static final int MESSAGE_ERROR = 1;
	public static final int MESSAGE_INFO = 2;

	MessageBox() {
	}

	public static void showMessageBoxInUIThread(final Component parentComponent, final Object message,
			final int messageType) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				showMessageBox(parentComponent, message, messageType);
			}
		});
	}

	private static ImageIcon getImageIconByType(int type) {
		switch (type) {
		case MESSAGE_ERROR:
			return new ImageIcon(ResourceManager.getInstance().getImageByName("msgbox-error.png"));
		case MESSAGE_INFO:
			return new ImageIcon(ResourceManager.getInstance().getImageByName("msgbox-info.png"));
		}
		return null;
	}

	public static void showMessageBox(final Component parentComponent, final Object message, int messageType) {
		JLabel labelMessage = new JLabel(message.toString(), getImageIconByType(messageType), JLabel.HORIZONTAL);
		final JComponent[] components = new JComponent[] { labelMessage };
		JOptionPane.showMessageDialog(parentComponent, components, "Message", JOptionPane.PLAIN_MESSAGE);
	}
}
