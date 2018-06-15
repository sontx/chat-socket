package com.blogspot.sontx.chatsocket.server.view;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.*;

/**
 * Tracks the log from {@link org.apache.log4j.Logger} log4j and shows to the UI.
 */
class Log4jUIAppender extends AppenderSkeleton {
    private final JTextPane logPane;

    Log4jUIAppender(JTextPane logPane) {
        this.logPane = logPane;
        setLayout(new PatternLayout("%m"));
    }

    private synchronized void appendLine(String msg) {
        String current = logPane.getText();
        if (current == null || current.length() == 0)
            logPane.setText(msg);
        else
            logPane.setText(current + System.lineSeparator() + msg);
    }

    private void appendLineInSafeThread(final String msg) {
        if (SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> appendLine(msg));
        } else {
            appendLine(msg);
        }
    }

    @Override
    protected void append(LoggingEvent event) {
        String formattedMessage = getLayout().format(event);
        appendLineInSafeThread(formattedMessage);
    }

    @Override
    public void close() {
        this.logPane.setText("");
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
