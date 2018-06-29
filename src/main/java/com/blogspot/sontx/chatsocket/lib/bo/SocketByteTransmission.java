package com.blogspot.sontx.chatsocket.lib.bo;

import com.blogspot.sontx.chatsocket.lib.utils.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Bytes socket communicating.
 */
public class SocketByteTransmission implements ByteTransmission {
    private Socket socket;
    private InputStream in;
    private OutputStream out;

    public SocketByteTransmission(Socket socket) throws IOException {
        this.socket = socket;
        in = socket.getInputStream();
        out = socket.getOutputStream();
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void close() throws IOException {
        socket.shutdownInput();
        socket.shutdownOutput();
        StreamUtils.tryCloseStream(in);
        StreamUtils.tryCloseStream(out);
        StreamUtils.tryCloseStream(socket);
    }

    @Override
    public void sendBytes(byte[] in) throws IOException {
        out.write(in);
    }

    @Override
    public int receiveBytes(byte[] out, int offset, int length) throws IOException {
        return in.read(out, offset, length);
    }

}
