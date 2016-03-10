package in.sontx.tut.chatsocket.bo;

import java.io.IOException;

import com.sun.istack.internal.NotNull;

public final class Protocol {
	public static final int DATA_IN_BUFFER_SIZE = 4096;
	private ObjectAdapter objectAdapter;
	private ITransmission transmission;

	public void sendObject(Object obj) throws IOException {
		transmission.sendBytes(objectAdapter.getBytes(obj));
	}

	public Object sendObjectForResult(Object obj, Class<?> objectClass) throws IOException {
		transmission.sendBytes(objectAdapter.getBytes(obj));
		return receiveObject(objectClass);
	}

	public Object sendObjectForResult(Object obj) throws IOException {
		return sendObjectForResult(obj, null);
	}

	public Object receiveObject(Class<?> objectClass) throws IOException {
		byte[] objectBytes = new byte[DATA_IN_BUFFER_SIZE];
		int realLength = transmission.receiveBytes(objectBytes, 0, objectBytes.length);
		if (realLength > 0) {
			byte[] realBytes = new byte[realLength];
			System.arraycopy(objectBytes, 0, realBytes, 0, realLength);
			return objectClass != null ? objectAdapter.getObject(realBytes, objectClass)
					: objectAdapter.getObject(realBytes);
		}
		return null;
	}

	public Object receiveObject() throws IOException {
		return receiveObject(null);
	}

	public Protocol(@NotNull ObjectAdapter objectAdapter, @NotNull ITransmission transmission) {
		this.objectAdapter = objectAdapter;
		this.transmission = transmission;
	}
}
