package com.blogspot.sontx.chatsocket.lib.utils;

public final class Task {
	public static void run(Runnable run) {
		Thread thread = new Thread(run);
		thread.setDaemon(true);
		thread.start();
	}

	private Task() {
	}
}
