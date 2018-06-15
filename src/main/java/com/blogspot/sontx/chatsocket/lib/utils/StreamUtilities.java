package com.blogspot.sontx.chatsocket.lib.utils;

import java.io.Closeable;
import java.io.IOException;

public final class StreamUtilities {
	public static void tryCloseStream(Closeable obj) {
		if (obj != null) {
			try {
				obj.close();
			} catch (IOException ignored) {
			}
		}
	}

	StreamUtilities() {
	}
}
