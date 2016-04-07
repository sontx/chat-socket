package in.sontx.tut.chatsocket.server.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import in.sontx.tut.chatsocket.bo.ResourceManager;
import in.sontx.tut.chatsocket.server.AccountManager;
import in.sontx.tut.chatsocket.server.Server;
import in.sontx.tut.chatsocket.utils.Log;
import in.sontx.tut.chatsocket.utils.StreamUtilities;
import in.sontx.tut.chatsocket.utils.Task;
import in.sontx.tut.chatsocket.utils.Log.ILogable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import javax.swing.ScrollPaneConstants;

public class Homechat extends JFrame implements ActionListener {
	private static final long serialVersionUID = -5733936059449135139L;
	private static final int MAX_CONNECTIONS = 100;
	private JTextField addressField;
	private JTextField portField;
	private JTextField workingField;
	private JTextPane logPane;
	private JButton btnShowIPs;
	private JButton btnStart;
	private JButton btnBrowse;
	private Server server = null;

	public Homechat() {
		setTitle("Homechat - Server");
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		JLabel label = new JLabel("");
		label.setBounds(7, 11, 191, 294);
		label.setIcon(new ImageIcon(ResourceManager.getInstance().getImageByName("banner.png")));
		getContentPane().add(label);

		JPanel panel = new JPanel();
		panel.setBounds(200, 11, 313, 128);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblIp = new JLabel("IP:");
		lblIp.setBounds(10, 15, 94, 14);
		panel.add(lblIp);

		addressField = new JTextField();
		addressField.setBounds(114, 12, 156, 20);
		panel.add(addressField);
		addressField.setColumns(10);

		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(10, 43, 94, 14);
		panel.add(lblPort);

		portField = new JTextField();
		portField.setText("3393");
		portField.setBounds(114, 40, 156, 20);
		panel.add(portField);
		portField.setColumns(10);

		JLabel lblWorkingDirectory = new JLabel("Working Directory:");
		lblWorkingDirectory.setBounds(10, 71, 94, 14);
		panel.add(lblWorkingDirectory);

		workingField = new JTextField();
		workingField.setBounds(114, 68, 156, 20);
		panel.add(workingField);
		workingField.setColumns(10);

		btnStart = new JButton("Start");
		btnStart.setBounds(214, 99, 89, 23);
		btnStart.addActionListener(this);
		panel.add(btnStart);

		btnBrowse = new JButton("...");
		btnBrowse.setBounds(280, 67, 25, 23);
		btnBrowse.addActionListener(this);
		panel.add(btnBrowse);

		btnShowIPs = new JButton("...");
		btnShowIPs.setBounds(280, 11, 25, 23);
		btnShowIPs.addActionListener(this);
		panel.add(btnShowIPs);

		JLabel lblCopyrightBySontx = new JLabel("Copyright by SONTX, www.sontx.in");
		lblCopyrightBySontx.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCopyrightBySontx.setForeground(Color.GRAY);
		lblCopyrightBySontx.setBounds(328, 321, 185, 14);
		getContentPane().add(lblCopyrightBySontx);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(208, 149, 297, 153);
		getContentPane().add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		logPane = new JTextPane();
		logPane.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(logPane);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel_1.add(scrollPane, BorderLayout.CENTER);

		setSize(529, 375);
		setLocationRelativeTo(null);
	}

	private void selectWorkingDirectory() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Where is your working directory?");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			workingField.setText(chooser.getSelectedFile().toString().replace("\\", "/"));
		}
	}

	private void selectAddress() {
		AddressDialog dialog = new AddressDialog(this);
		dialog.setOnSelectedAddressListener(new AddressDialog.OnSelectedAddressListener() {
			@Override
			public void onSelectedAddress(String address) {
				addressField.setText(address);
			}
		});
		dialog.setVisible(true);
	}

	private boolean isNumber(String s) {
		if (s == null)
			return false;
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
		}
		return false;
	}

	private boolean checkInput() {
		String address = addressField.getText();
		String port = portField.getText();
		String workingDir = workingField.getText();
		if (address == null || address.length() == 0)
			return false;
		if (!isNumber(port))
			return false;
		if (workingDir == null || workingDir.length() == 0)
			return false;
		return true;
	}

	private void changeInputState(boolean inputable) {
		btnStart.setText(inputable ? "Start" : "Stop");
		addressField.setEditable(inputable);
		portField.setEditable(inputable);
		workingField.setEditable(inputable);
	}

	private void setWorkingDir(String dir) {
		dir = dir.replace('\\', '/');
		AccountManager.createInstance(dir);
		if (Log.getLogable() instanceof ServerLogable)
			StreamUtilities.tryCloseStream(((ServerLogable) Log.getLogable()));
		Log.setLogable(new ServerLogable(logPane, dir));
	}

	private void startServer() {
		if (!checkInput()) {
			JOptionPane.showMessageDialog(this, "Wrong input!");
		} else {
			changeInputState(false);
			final int port = Integer.parseInt(portField.getText());
			final String address = addressField.getText();
			setWorkingDir(workingField.getText());
			Task.run(new Runnable() {
				@Override
				public void run() {
					try {
						server = new Server(port, address, MAX_CONNECTIONS);
						server.waitForConnection();
					} catch (IOException e) {
						stopServer();
					}
				}
			});
		}
	}

	private void stopServer() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				changeInputState(true);
				if (server != null) {
					StreamUtilities.tryCloseStream(server);
					server = null;
				}
			}
		});
	}

	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			ILogable logable = Log.getLogable();
			if (logable instanceof ServerLogable)
				StreamUtilities.tryCloseStream(((ServerLogable) Log.getLogable()));
			stopServer();
		}
		super.processWindowEvent(e);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnShowIPs)) {
			selectAddress();
		} else if (e.getSource().equals(btnBrowse)) {
			selectWorkingDirectory();
		} else if (e.getSource().equals(btnStart)) {
			if (server != null)
				stopServer();
			else
				startServer();
		}
	}

	private static class ServerLogable implements Log.ILogable, Closeable {
		private static final String LOG_FILENAME = "chatlog.log";
		private final JTextPane logPane;
		private BufferedWriter writer = null;

		public ServerLogable(JTextPane textPane, String dir) {
			this.logPane = textPane;
			try {
				if (!dir.endsWith("/"))
					dir = dir + "/";
				writer = new BufferedWriter(new FileWriter(dir + LOG_FILENAME));
			} catch (IOException e) {
				appendLineInSafeThraed(String.format("Can't create log file with error '%s'", e.getMessage()));
			}
		}

		private String getMessage(Object obj) {
			return obj != null ? obj.toString() : "NULL";
		}

		private void appendLine(String msg) {
			String current = logPane.getText();
			if (current == null || current.length() == 0)
				logPane.setText(msg);
			else
				logPane.setText(current + System.lineSeparator() + msg);
			System.out.println(msg);
		}

		private void appendLineInSafeThraed(final String msg) {
			if (SwingUtilities.isEventDispatchThread()) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						appendLine(msg);
					}
				});
			} else {
				appendLine(msg);
			}
		}

		private synchronized void appendToFile(String msg) {
			if (writer != null) {
				try {
					writer.write(msg);
					writer.newLine();
					writer.flush();
				} catch (IOException e) {
				}
			}
		}

		@Override
		public void i(Object obj) {
			String msg = getMessage(obj);
			appendLineInSafeThraed(String.format("INFO: %s", msg));
		}

		@Override
		public void l(Object obj) {
			String msg = getMessage(obj);
			//appendLineInSafeThraed(String.format("LOG: %s", msg));
			appendToFile(msg);
		}

		@Override
		public void close() throws IOException {
			if (writer != null) {
				writer.close();
			}
		}

	}
}
