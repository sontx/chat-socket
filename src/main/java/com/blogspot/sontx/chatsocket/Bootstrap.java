package com.blogspot.sontx.chatsocket;

import com.blogspot.sontx.chatsocket.client.AppClientImpl;
import com.blogspot.sontx.chatsocket.client.platform.ClientJavaFxPlatform;
import com.blogspot.sontx.chatsocket.client.platform.ClientSwingPlatform;
import com.blogspot.sontx.chatsocket.server.AppServerImpl;
import com.blogspot.sontx.chatsocket.server.platform.ServerJavaFxPlatform;
import com.blogspot.sontx.chatsocket.server.platform.ServerSwingPlatform;
import lombok.extern.log4j.Log4j;
import org.apache.commons.cli.*;

/**
 * Starts client/server based on application's arguments.
 * <p>
 * Supported arguments:
 * <pre>
 * -h (--help): shows help.
 * -m (--mode): starts client/server, the following flag either <code>client</code> or <code>server</code>
 * -ui(--uistyle): shows the app with java <code>swing</code> or <code>javafx</code>.
 * </pre>
 * </p>
 */
@Log4j
class Bootstrap {
    private Options options;

    Bootstrap() {
        initializeOptions();
    }

    private void initializeOptions() {
        options = new Options();
        options.addOption("h", "help", false, "Show help.");
        options.addRequiredOption("m", "mode", true, "The app should be run as client/server mode.");
        options.addOption("ui", "uistyle", true, "UI style will be shown (swing/javafx)");
    }

    void boot(String[] args) {
        CommandLine commandLine = parseArgs(args);
        if (commandLine == null || commandLine.hasOption("h")) {
            showHelp();
        } else if (commandLine.hasOption("m")) {
            String mode = commandLine.getOptionValue("m");
            if ("client".equals(mode))
                startClient(commandLine.getOptionValue("ui"));
            else if ("server".equals(mode))
                startServer(commandLine.getOptionValue("ui"));
            else
                showHelp();
        }
    }

    private void startClient(String uistyle) {
        new AppClientImpl().start("swing".equals(uistyle) ? new ClientSwingPlatform() : new ClientJavaFxPlatform());
    }

    private void startServer(String uistyle) {
        new AppServerImpl().start("swing".equals(uistyle) ? new ServerSwingPlatform() : new ServerJavaFxPlatform());
    }

    private CommandLine parseArgs(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            log.error("Wrong app arguments", e);
            return null;
        }
    }

    private void showHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(AppConfig.getDefault().getAppName(), options);
    }
}
