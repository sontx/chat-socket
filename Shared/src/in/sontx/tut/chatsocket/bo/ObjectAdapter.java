package in.sontx.tut.chatsocket.bo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.sun.istack.internal.NotNull;

import in.sontx.tut.chatsocket.utils.StreamUtilities;

public final class ObjectAdapter {
	private IAdapter adapter = new SerializableAdapter();

	public byte[] getBytes(Object obj) {
		return adapter != null ? adapter.objectToBytes(obj) : null;
	}

	public Object getObject(byte[] bytes) {
		return adapter != null ? adapter.bytesToObject(bytes) : null;
	}

	public Object getObject(byte[] bytes, Class<?> objectClass) {
		Object rawObject = getObject(bytes);
		return rawObject != null && objectClass.isAssignableFrom(rawObject.getClass()) ? rawObject : null;
	}

	public static class SerializableAdapter implements IAdapter {

		@Override
		public byte[] objectToBytes(Object obj) {
			ByteArrayOutputStream byteArrayOutputStream = null;
			ObjectOutputStream objectOutputStream = null;
			byte[] resultBytes = null;
			try {
				byteArrayOutputStream = new ByteArrayOutputStream();
				objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
				objectOutputStream.writeObject(obj);
				resultBytes = byteArrayOutputStream.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				StreamUtilities.tryCloseStream(byteArrayOutputStream);
				StreamUtilities.tryCloseStream(objectOutputStream);
			}
			return resultBytes;
		}

		@Override
		public Object bytesToObject(byte[] bytes) {
			ByteArrayInputStream byteArrayInputStream = null;
			ObjectInputStream objectInputStream = null;
			Object resultObject = null;
			try {
				byteArrayInputStream = new ByteArrayInputStream(bytes);
				objectInputStream = new ObjectInputStream(byteArrayInputStream);
				resultObject = objectInputStream.readObject();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				StreamUtilities.tryCloseStream(byteArrayInputStream);
				StreamUtilities.tryCloseStream(objectInputStream);
			}
			return resultObject;
		}

	}

	public interface IAdapter {
		byte[] objectToBytes(@NotNull Object obj);

		Object bytesToObject(@NotNull byte[] bytes);
	}
}
