package com.blogspot.sontx.chatsocket.lib.service;

public abstract class BackgroundService extends AbstractService implements Runnable {
    private Thread thread;

    @Override
    public synchronized void start() {
        if (isStarted()) return;
        super.start();
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public synchronized void stop() {
        if (!isStarted()) return;
        super.stop();
        thread.interrupt();
    }

    public abstract void run();
}
