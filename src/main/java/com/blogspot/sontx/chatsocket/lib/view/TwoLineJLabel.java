package com.blogspot.sontx.chatsocket.lib.view;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import javax.swing.*;

public class TwoLineJLabel extends JLabel {
    protected void setText(String line1, String line2) {
        String escapedLine1 = StringUtils.isEmpty(line1) ? "" : StringEscapeUtils.escapeHtml3(line1);
        String escapedLine2 = StringUtils.isEmpty(line2) ? "" : StringEscapeUtils.escapeHtml3(line2);
        setText(String.format("<html><b>%s</b><br>%s</html>", escapedLine1, escapedLine2));
    }
}
