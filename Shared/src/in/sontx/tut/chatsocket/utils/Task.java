package in.sontx.tut.chatsocket.utils;

import com.sun.istack.internal.NotNull;

public final class Task {
	Task() {
	}

	public static void run(@NotNull Runnable run) {
		Thread thread = new Thread(run);
		thread.setDaemon(true);
		thread.start();
	}
}
