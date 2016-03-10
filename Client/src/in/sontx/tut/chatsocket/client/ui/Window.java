package in.sontx.tut.chatsocket.client.ui;

import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import in.sontx.tut.chatsocket.client.Application;

public abstract class Window extends JFrame {
	private static final long serialVersionUID = 7150002275569612951L;

	protected abstract void initializeComponents();

	public Window() {
		Application.registerWindow(this);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initializeComponents();
	}

	@Override
	public void setDefaultCloseOperation(int operation) {
		if (operation != EXIT_ON_CLOSE)
			super.setDefaultCloseOperation(operation);
	}

	protected void onWindowClosing() {
		Application.unregisterWindow(this);
	}

	@Override
	public void dispose() {
		onWindowClosing();
		super.dispose();
	}

	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING && getDefaultCloseOperation() == DISPOSE_ON_CLOSE) {
			onWindowClosing();
		}
		super.processWindowEvent(e);
	}
}
