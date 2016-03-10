package in.sontx.tut.chatsocket.client.ui;

import javax.swing.JProgressBar;
import java.awt.BorderLayout;
import java.awt.Component;

public class ProcessingDialog extends Window {
	private static final long serialVersionUID = -5422200050849086407L;
	private static ProcessingDialog instance = null;

	public static void showBox(Component owner, String message) {
		if (instance != null && !instance.isVisible())
			hideBox();
		instance = new ProcessingDialog();
		instance.setTitle(message);
		instance.setLocationRelativeTo(owner);
		instance.setVisible(true);
	}

	public static void hideBox() {
		if (instance != null) {
			instance.dispose();
			instance = null;
		}
	}

	public ProcessingDialog() {
		
	}

	@Override
	protected void initializeComponents() {
		setResizable(false);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		getContentPane().add(progressBar, BorderLayout.CENTER);

		setSize(350, 50);
	}
}
