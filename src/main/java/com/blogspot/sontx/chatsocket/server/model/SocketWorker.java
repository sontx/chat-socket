package com.blogspot.sontx.chatsocket.server.model;

import com.blogspot.sontx.chatsocket.lib.bean.*;
import com.blogspot.sontx.chatsocket.lib.bo.DefaultObjectTransmission;
import com.blogspot.sontx.chatsocket.lib.bo.SerializableObjectAdapter;
import com.blogspot.sontx.chatsocket.lib.bo.SocketByteTransmission;
import com.blogspot.sontx.chatsocket.lib.service.BackgroundService;
import com.blogspot.sontx.chatsocket.lib.utils.StreamUtils;
import com.blogspot.sontx.chatsocket.server.event.*;
import com.google.common.eventbus.Subscribe;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.net.Socket;

/**
 * Default implementation of {@link Worker} based on {@link Socket}.
 * Each {@link SocketWorker} will be started in a separated thread.
 */
@Log4j
public class SocketWorker extends BackgroundService implements Worker {
    private final DefaultObjectTransmission objectTransmission;
    private final int sessionId;
    private final Object lock = new Object();
    private Profile account;
    private volatile boolean closing;
    private volatile boolean alreadyShutdown;

    SocketWorker(Socket socket, int sessionId) throws IOException {
        objectTransmission = new DefaultObjectTransmission(
                new SerializableObjectAdapter(),
                new SocketByteTransmission(socket));
        this.sessionId = sessionId;
    }

    @Override
    public void run() {
        try {

            waitForRequests();
        } catch (Exception e) {
            if (!closing)
                log.error("Error while running worker", e);
        } finally {
            shutdownWorker();
        }
    }

    /**
     * All incomming requests from a individual client will be received and broadcast to higher layer.
     */
    private void waitForRequests() throws IOException {
        while (!closing) {
            Object receivedObject = objectTransmission.receiveObject();
            if (receivedObject == null)
                break;
            if (receivedObject instanceof Request) {
                post(new RequestReceivedEvent((Request) receivedObject, account, this));
            }
        }
    }

    @Subscribe
    public void onShutdownWorker(ShutdownWorkerEvent event) {
        if (event.getServerSession() == sessionId)
            shutdownWorker();
    }

    private boolean itIsMe(String accountId) {
        synchronized (lock) {
            return account != null && account.getId().equals(accountId);
        }
    }

    private synchronized void shutdownWorker() {
        if (alreadyShutdown) return;

        closing = true;

        StreamUtils.tryCloseStream(objectTransmission);
        Profile profile = account;
        if (profile != null) {
            profile.setState(Profile.STATE_OFFLINE);
            post(new AccountInfoChangedEvent(profile));
        }

        alreadyShutdown = true;

        stop();
    }

    @Subscribe
    public void onForwardChatMessage(ForwardChatMessageEvent event) {
        if (itIsMe(event.getReceiverId())) {
            Response result = new Response();
            result.setRequestCode(RequestCode.ChatMessage);
            try {
                sendChatMessage(event.getForwardMessage());
                result.setCode(ResponseCode.OK);
            } catch (Exception e) {
                log.error("Error while forwarding chat message", e);
                result.setExtra("Friend's connection broken down.");
                result.setCode(ResponseCode.Fail);
                shutdownWorker();
            } finally {
                event.setResponseResult(result);
            }
        }
    }

    private void sendChatMessage(ChatMessage forwardMessage) throws IOException {
        Response response = new Response();
        response.setCode(ResponseCode.OK);
        response.setRequestCode(RequestCode.ChatMessage);
        response.setExtra(forwardMessage);
        response(response);
    }

    @Subscribe
    public void onLookupWorker(LookupWorkerEvent event) {
        if (itIsMe(event.getMatchedAccount().getId())) {
            event.setMatchedWorker(this);
        }
    }

    @Override
    public void response(Object obj) throws IOException {
        try {
            objectTransmission.sendObject(obj);
        } catch (Exception e) {
            shutdownWorker();
            log.error("Error while response to client", e);
            throw e;
        }
    }

    @Override
    public void setAccount(Profile account) {
        synchronized (lock) {
            this.account = account;
        }
    }

    @Subscribe
    public void onAccountInfoChanged(AccountInfoChangedEvent event) {
        runAsync(() -> {
            if (!itIsMe(event.getProfile().getId())) {
                Response result = new Response();
                result.setCode(ResponseCode.OK);
                result.setRequestCode(RequestCode.FriendInfoUpdated);
                result.setExtra(event.getProfile());
                try {
                    response(result);
                } catch (IOException e) {
                    log.error("Error while broadcast account info changed to workers", e);
                    shutdownWorker();
                }
            }
        });
    }
}
