package com.blogspot.sontx.chatsocket.lib.view;

import org.apache.commons.text.StringEscapeUtils;

import javax.swing.*;

public class TwoLineJLabel extends JLabel {
	private static final long serialVersionUID = 5631622345330232637L;

	public void setText(String line1, String line2) {
		setText(String.format("<html><b>%s</b><br>%s</html>", line1, StringEscapeUtils.escapeHtml3(line2)));
	}
}
