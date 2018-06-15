package com.blogspot.sontx.chatsocket.client.view;

import com.blogspot.sontx.chatsocket.lib.Callback;
import com.blogspot.sontx.chatsocket.lib.bo.ImagesResource;
import com.blogspot.sontx.chatsocket.lib.view.BaseWindow;
import com.blogspot.sontx.chatsocket.lib.view.MessageBox;
import org.apache.commons.text.StringEscapeUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ChatWindow extends BaseWindow implements
        ChatView,
        ActionListener,
        WindowStateListener,
        AdjustmentListener {

    private static final String STYLE_SHEET
            = ".chat-box { margin: 2px; }"
            + ".chat-box p { display: block; padding: 7px; margin-top: 2px; margin-bottom: 2px; word-wrap: break-word;}"
            + ".chat-msg1 { background-color: #add8e6; }" + ".chat-msg2 { background-color: #e6adb5; }";

    private static final String DEFAULT_HTML_FORMAT
            = "<style>" + STYLE_SHEET + "</style>"
            + "<div id=content class=chat-box> </div>";

    private static final String DEFAULT_FRIEND_CHAT_FORMAT = "<p class=chat-msg1>%s</p>";
    private static final String DEFAULT_WE_CHAT_FORMAT = "<p class=chat-msg2>%s</p>";

    private JTextField inputField;
    private JTextPane displayField;
    private Callback<String> sendButtonClickListener;

    @Override
    protected void initializeComponents() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(panel, BorderLayout.SOUTH);
        panel.setLayout(new BorderLayout(5, 5));

        inputField = new JTextField();
        inputField.setToolTipText("Enter your chat content then enter to send");
        panel.add(inputField, BorderLayout.CENTER);
        inputField.addActionListener(this);

        JButton btnSend = new JButton("");
        btnSend.setIcon(new ImageIcon(ImagesResource.getInstance().getImageByName("button-send.png")));
        btnSend.setToolTipText("Press to send your chat content");
        btnSend.addActionListener(this);
        panel.add(btnSend, BorderLayout.EAST);

        displayField = new JTextPane();
        displayField.setContentType("text/html");
        displayField.setEditable(false);
        displayField.setText(DEFAULT_HTML_FORMAT);

        JScrollPane scrollPane = new JScrollPane(displayField);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        scrollPane.getVerticalScrollBar().addAdjustmentListener(this);

        setSize(280, 400);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (sendButtonClickListener != null)
            sendButtonClickListener.call(inputField.getText());
    }

    @Override
    public void windowStateChanged(WindowEvent e) {
        if (e.getNewState() == WindowEvent.WINDOW_ACTIVATED) {
            inputField.requestFocus();
        }
    }

    private String prepareHtmlString(String rawContent) {
        return StringEscapeUtils.escapeXml10(rawContent);
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        e.getAdjustable().setValue(e.getAdjustable().getMaximum());
    }

    private void appendRawHtmlChatContent(String html) {
        HTMLDocument document = (HTMLDocument) displayField.getDocument();
        Element contentElement = document.getElement("content");
        try {
            if (contentElement.getElementCount() > 0) {
                Element lastElement = contentElement.getElement(contentElement.getElementCount() - 1);
                document = (HTMLDocument) contentElement.getDocument();
                document.insertAfterEnd(lastElement, html);
            } else {
                document.insertAfterStart(contentElement, html);
            }
        } catch (BadLocationException | IOException e) {
            MessageBox.show(this, "Internal error: " + e.getMessage(), MessageBox.MESSAGE_ERROR);
        }
    }

    @Override
    public void appendFriendMessage(String message) {
        String htmlMessage = prepareHtmlString(message);
        appendRawHtmlChatContent(String.format(DEFAULT_FRIEND_CHAT_FORMAT, htmlMessage));
    }

    @Override
    public void appendMeMyMessage(String message) {
        String htmlMessage = prepareHtmlString(message);
        appendRawHtmlChatContent(String.format(DEFAULT_WE_CHAT_FORMAT, htmlMessage));
    }

    @Override
    public void setSendButtonClickListener(Callback<String> listener) {
        this.sendButtonClickListener = listener;
    }

    @Override
    public void clearInput() {
        inputField.setText("");
    }
}
