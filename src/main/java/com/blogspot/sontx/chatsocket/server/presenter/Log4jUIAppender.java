package com.blogspot.sontx.chatsocket.server.presenter;

import com.blogspot.sontx.chatsocket.server.view.LogView;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.*;

/**
 * Tracks the log from {@link org.apache.log4j.Logger} log4j and shows to the UI.
 */
class Log4jUIAppender extends AppenderSkeleton {
    private final LogView logView;

    Log4jUIAppender(LogView logView) {
        this.logView = logView;
        setLayout(new PatternLayout("%m"));
    }

    private void appendLine(String msg) {
        logView.appendLog(msg);
    }

    @Override
    protected void append(LoggingEvent event) {
        String formattedMessage = getLayout().format(event);
        appendLine(formattedMessage);
    }

    @Override
    public void close() {
        logView.clearLog();
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
