package com.blogspot.sontx.chatsocket;

import com.blogspot.sontx.chatsocket.client.AppClientImpl;
import com.blogspot.sontx.chatsocket.client.platform.ClientJavaFxPlatform;
import com.blogspot.sontx.chatsocket.server.AppServerImpl;
import com.blogspot.sontx.chatsocket.server.platform.ServerJavaFxPlatform;
import lombok.extern.log4j.Log4j;
import org.apache.commons.cli.*;

/**
 * Starts client/server based on application's arguments.
 * <p>
 * Supported arguments:
 * <pre>
 * -h (--help): shows help.
 * -m (--mode): starts client/server, the following flag either <code>client</code> or <code>server</code>
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
    }

    void boot(String[] args) {
        CommandLine commandLine = parseArgs(args);
        if (commandLine == null || commandLine.hasOption("h")) {
            showHelp();
        } else if (commandLine.hasOption("m")) {
            String mode = commandLine.getOptionValue("m");
            if ("client".equals(mode))
                startClient();
            else if ("server".equals(mode))
                startServer();
            else
                showHelp();
        }
    }

    private void startClient() {
        new AppClientImpl().start(new ClientJavaFxPlatform());
    }

    private void startServer() {
        new AppServerImpl().start(new ServerJavaFxPlatform());
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
