package com.blogspot.sontx.chatsocket;

import com.blogspot.sontx.chatsocket.client.AppClientImpl;
import com.blogspot.sontx.chatsocket.client.platform.ClientJavaFxPlatform;
import com.blogspot.sontx.chatsocket.client.platform.ClientSwingPlatform;
import com.blogspot.sontx.chatsocket.server.AppServerImpl;
import com.blogspot.sontx.chatsocket.server.platform.ServerJavaFxPlatform;
import com.blogspot.sontx.chatsocket.server.platform.ServerSwingPlatform;
import lombok.extern.log4j.Log4j;
import org.apache.commons.cli.*;

import java.util.Scanner;

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
        options.addOption("m", "mode", true, "The app should be run as client/server mode.");
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
        } else {
            askUser();
        }
    }

    private void askUser() {
        System.out.print("Working mode was not supplied, please enter your mode (client/server): ");
        Scanner scanner = new Scanner(System.in);
        String mode = scanner.nextLine();
        if (mode.equals("client"))
            startClient(null);
        else if (mode.equals("server"))
            startServer(null);
    }

    private void startClient(String uiStyle) {
        log.info("Starting app as client");
        new AppClientImpl().start("swing".equals(uiStyle) ? new ClientSwingPlatform() : new ClientJavaFxPlatform());
    }

    private void startServer(String uiStyle) {
        log.info("Starting app as server");
        new AppServerImpl().start("swing".equals(uiStyle) ? new ServerSwingPlatform() : new ServerJavaFxPlatform());
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
