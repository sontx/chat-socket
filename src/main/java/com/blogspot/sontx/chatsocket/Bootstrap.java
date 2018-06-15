package com.blogspot.sontx.chatsocket;

import com.blogspot.sontx.chatsocket.client.AppClientImpl;
import com.blogspot.sontx.chatsocket.server.AppServerImpl;
import lombok.extern.log4j.Log4j;
import org.apache.commons.cli.*;

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
        try {
            new AppClientImpl().start();
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void startServer() {
        try {
            new AppServerImpl().start();
        } catch (Exception e) {
            log.error(e);
        }
    }

    private CommandLine parseArgs(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            log.error(e);
            return null;
        }
    }

    private void showHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(AppConfig.getDefault().getAppName(), options);
    }
}
