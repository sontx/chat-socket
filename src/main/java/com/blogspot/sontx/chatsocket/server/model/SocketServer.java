package com.blogspot.sontx.chatsocket.server.model;

import com.blogspot.sontx.chatsocket.lib.service.BackgroundService;
import com.blogspot.sontx.chatsocket.lib.utils.StreamUtils;
import com.blogspot.sontx.chatsocket.server.event.ShutdownServerEvent;
import com.blogspot.sontx.chatsocket.server.event.ShutdownWorkerEvent;
import lombok.extern.log4j.Log4j;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Default implementation of {@link Server} based on {@link Socket}.
 * Each {@link SocketServer} will be started in a separated thread.
 */
@Log4j
public class SocketServer extends BackgroundService implements Server {
    private static AtomicInteger freeId = new AtomicInteger();

    private final ServerSocket serverSocket;
    private final int sessionId;

    private volatile boolean closing;
    private volatile boolean alreadyShutdown;

    public SocketServer(int port, String address, int maxConnection) throws IOException {
        serverSocket = new ServerSocket(port, maxConnection, InetAddress.getByName(address));
        sessionId = freeId.incrementAndGet();
        log.debug("Server session is " + sessionId);
    }

    @Override
    public void run() {
        log.debug("ServerSocket is started");
        try {
            waitForConnections();
        } catch (Exception e) {
            if (!closing)
                log.error("Server socket has stopped", e);
        } finally {
            shutdownServer();
        }
    }

    private synchronized void shutdownServer() {
        if (alreadyShutdown) return;

        closing = true;

        log.debug("ServerSocket is stopped");

        post(new ShutdownWorkerEvent(sessionId));
        StreamUtils.tryCloseStream(serverSocket);

        alreadyShutdown = true;

        stop();
    }

    private void waitForConnections() throws IOException {
        log.debug("Waiting for connections...");
        Socket socket;
        while ((socket = serverSocket.accept()) != null) {
            log.debug("Accepted " + socket.getRemoteSocketAddress().toString());
            startWorker(socket);
        }
    }

    private void startWorker(Socket socket) {
        try {
            Worker worker = new SocketWorker(socket, sessionId).build(getPlatform());
            worker.start();
            log.debug("Start new worker");
        } catch (IOException e) {
            if (!closing)
                log.error("Worker has stopped", e);
            StreamUtils.tryCloseStream(socket);
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onShutdownServer(ShutdownServerEvent event) {
        if (event.getSessionId() == ShutdownServerEvent.ALL || event.getSessionId() == sessionId)
            shutdownServer();
    }

    @Override
    public void close() {
        shutdownServer();
    }
}
