package in.sontx.tut.chatsocket.client.ui;

import javax.swing.ImageIcon;

import in.sontx.tut.chatsocket.bean.AccountInfo;
import in.sontx.tut.chatsocket.bean.ChatRequest;
import in.sontx.tut.chatsocket.bean.ChatResult;
import in.sontx.tut.chatsocket.bo.ResourceManager;
import in.sontx.tut.chatsocket.client.Application;
import in.sontx.tut.chatsocket.view.ClickableJLabel;
import in.sontx.tut.chatsocket.view.FitImageJLabel;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Color;

public class ProfileWindow extends ProcessingWindow implements ActionListener {

	private static final long serialVersionUID = 7283676895165142142L;
	private JTextField statusField;
	private JTextField dispNameField;
	private ClickableJLabel passwordButton;
	private ClickableJLabel dispNameButton;
	private ClickableJLabel statusButton;
	private JLabel usernameField;
	private JLabel lblDisplayName;

	@Override
	protected void initializeComponents() {
		setTitle("My profile");
		setResizable(false);
		getContentPane().setLayout(null);

		FitImageJLabel avatarField = new FitImageJLabel();
		avatarField.setBounds(10, 11, 101, 150);
		avatarField.setIcon(new ImageIcon(ResourceManager.getInstance().getImageByName("profile.png")));
		getContentPane().add(avatarField);

		dispNameField = new JTextField();
		dispNameField.setText("Handsome man");
		dispNameField.setBounds(203, 94, 164, 20);
		getContentPane().add(dispNameField);
		dispNameField.setColumns(10);

		statusField = new JTextField();
		statusField.setBounds(203, 119, 164, 20);
		getContentPane().add(statusField);
		statusField.setColumns(10);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(121, 44, 72, 14);
		getContentPane().add(lblUsername);

		usernameField = new JLabel("anonymous");
		usernameField.setFont(new Font("Tahoma", Font.BOLD, 11));
		usernameField.setBounds(203, 44, 164, 14);
		getContentPane().add(usernameField);

		lblDisplayName = new JLabel("Display name:");
		lblDisplayName.setBounds(121, 97, 72, 14);
		getContentPane().add(lblDisplayName);

		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setBounds(121, 122, 72, 14);
		getContentPane().add(lblStatus);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(121, 69, 72, 14);
		getContentPane().add(lblPassword);

		JLabel label = new JLabel("*******");
		label.setBounds(203, 72, 46, 14);
		getContentPane().add(label);

		passwordButton = new ClickableJLabel("Change...");
		passwordButton.setForeground(Color.BLUE);
		passwordButton.setHorizontalAlignment(SwingConstants.RIGHT);
		passwordButton.setBounds(374, 69, 60, 14);
		passwordButton.addActionListener(this);
		getContentPane().add(passwordButton);

		dispNameButton = new ClickableJLabel("Change...");
		dispNameButton.setHorizontalAlignment(SwingConstants.RIGHT);
		dispNameButton.setForeground(Color.BLUE);
		dispNameButton.setBounds(377, 97, 57, 14);
		dispNameButton.addActionListener(this);
		getContentPane().add(dispNameButton);

		statusButton = new ClickableJLabel("Change...");
		statusButton.setHorizontalAlignment(SwingConstants.RIGHT);
		statusButton.setForeground(Color.BLUE);
		statusButton.setBounds(377, 122, 57, 14);
		statusButton.addActionListener(this);
		getContentPane().add(statusButton);

		JButton btnLookGood = new JButton("Look good");
		btnLookGood.setBounds(345, 237, 89, 23);
		btnLookGood.addActionListener(this);
		getContentPane().add(btnLookGood);

		setSize(450, 300);
	}

	public ProfileWindow() {
	}

	public void setProfileInfo(AccountInfo accountInfo, String username) {
		usernameField.setText(username);
		dispNameField.setText(accountInfo.getDisplayName());
		statusField.setText(accountInfo.getStatus());
	}

	private boolean checkDisplayName(String s) {
		return s != null && s.trim().length() > 0;
	}

	private boolean checkStatus(String s) {
		return s != null && s.trim().length() > 0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(passwordButton)) {
			Application.showWindow(PasswordWindow.class);
		} else if (e.getSource().equals(dispNameButton)) {
			if (checkDisplayName(dispNameField.getText()))
				changeDisplayName(dispNameField.getText());
		} else if (e.getSource().equals(statusButton)) {
			if (checkStatus(statusField.getText()))
				changeStatus(statusField.getText());
		} else {
			dispose();
		}
	}

	private void changeStatus(String status) {
		ChatRequest request = new ChatRequest(ChatRequest.CODE_CHANGE_STATUS, status);
		doInBackground(request);
	}

	private void changeDisplayName(String dispname) {
		ChatRequest request = new ChatRequest(ChatRequest.CODE_CHANGE_DISPNAME, dispname);
		doInBackground(request);
	}

	@Override
	protected void doneBackgoundTask(ChatResult result) {
		if (result.getCode() == ChatResult.CODE_OK) {
			MessageBox.showMessageBoxInUIThread(this, "Applied your changes!", MessageBox.MESSAGE_INFO);
		} else {
			MessageBox.showMessageBoxInUIThread(this, "Did not apply your changes!", MessageBox.MESSAGE_ERROR);
		}
		setVisible(true);
	}
}
