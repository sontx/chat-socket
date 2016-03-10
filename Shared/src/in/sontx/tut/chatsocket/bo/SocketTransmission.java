package in.sontx.tut.chatsocket.bo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import in.sontx.tut.chatsocket.utils.StreamUtilities;

public class SocketTransmission implements ITransmission {
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	
	public Socket getSocket() {
		return socket;
	}
	
	public SocketTransmission(Socket socket) throws IOException {
		this.socket = socket;
		in = socket.getInputStream();
		out = socket.getOutputStream();
	}
	
	@Override
	public void close() throws IOException {
		StreamUtilities.tryCloseStream(in);
		StreamUtilities.tryCloseStream(out);
		StreamUtilities.tryCloseStream(socket);
	}

	@Override
	public void sendBytes(byte[] in) throws IOException {
		out.write(in);
	}

	@Override
	public int receiveBytes(byte[] out, int offset, int length) throws IOException {
		return in.read(out, offset, length);
	}

	@Override
	public boolean ready() throws IOException {
		return in.available() > 0;
	}

}
