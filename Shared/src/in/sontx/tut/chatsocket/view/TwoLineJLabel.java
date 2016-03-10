package in.sontx.tut.chatsocket.view;

import javax.swing.JLabel;

import org.apache.commons.lang3.StringEscapeUtils;

public class TwoLineJLabel extends JLabel {
	private static final long serialVersionUID = 5631622345330232637L;

	public void setText(String line1, String line2) {
		setText(String.format("<html><b>%s</b><br>%s</html>", line1, StringEscapeUtils.escapeHtml3(line2)));
	}
}
