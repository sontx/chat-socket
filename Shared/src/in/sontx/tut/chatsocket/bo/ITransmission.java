package in.sontx.tut.chatsocket.bo;

import java.io.Closeable;
import java.io.IOException;

public interface ITransmission extends Closeable {
	void sendBytes(byte[] in) throws IOException;
	int receiveBytes(byte[] out, int offset, int length) throws IOException;
	boolean ready() throws IOException;
}
