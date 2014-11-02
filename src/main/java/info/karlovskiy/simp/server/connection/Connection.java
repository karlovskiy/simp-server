package info.karlovskiy.simp.server.connection;

import info.karlovskiy.simp.server.request.RequestType;
import info.karlovskiy.simp.server.response.ErrorType;
import info.karlovskiy.simp.server.response.ResponseType;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Set;

import static info.karlovskiy.simp.server.ServerProperties.ENCODING;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 10/25/14
 */
public class Connection implements Closeable {

    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedInputStream(socket.getInputStream());
        this.out = new BufferedOutputStream(socket.getOutputStream());
    }

    public byte[] readHeader() throws IOException {
        byte[] header = new byte[2];
        int bytesRead = 0;
        while (bytesRead != header.length) {
            int read = in.read(header, bytesRead, header.length - bytesRead);
            if (read == -1) {
                throw new IOException("Error reading header, stream closed");
            }
            bytesRead += read;
        }
        if (header[0] != 1) {
            throw new IOException("Unsupported protocol version");
        }
        byte code = header[1];
        RequestType requestType = RequestType.valueOf(code);
        if (requestType == null) {
            throw new IOException("Null request type: " + code);
        }
        return header;
    }

    public String readUser() throws IOException {
        int bytesRead = in.read();
        if (bytesRead == -1) {
            throw new IOException("Error reading user length, stream closed");
        }
        byte[] buff = new byte[bytesRead];
        bytesRead = 0;
        while (bytesRead != buff.length) {
            int read = in.read(buff, bytesRead, buff.length - bytesRead);
            if (read == -1) {
                throw new IOException("Error reading user name, stream closed");
            }
            bytesRead += read;
        }
        String user = new String(buff, ENCODING);
        return user;
    }

    public byte[] readMessage() throws IOException {
        byte[] buff = new byte[4];
        int bytesRead = 0;
        while (bytesRead != buff.length) {
            int read = in.read(buff, bytesRead, buff.length - bytesRead);
            if (read == -1) {
                throw new IOException("Error reading message length, stream closed");
            }
            bytesRead += read;
        }
        int messageLength = ByteBuffer.wrap(buff).getInt();
        buff = new byte[messageLength];
        bytesRead = 0;
        while (bytesRead != buff.length) {
            int read = in.read(buff, bytesRead, buff.length - bytesRead);
            if (read == -1) {
                throw new IOException("Error reading message, stream closed");
            }
            bytesRead += read;
        }
        return buff;
    }

    public String readUserConnected() throws IOException {

        byte[] header = readHeader();
        byte code = header[1];
        RequestType requestType = RequestType.valueOf(code);
        if (requestType != RequestType.CONNECT) {
            throw new IOException("Wrong request type: " + code);
        }
        String user = readUser();
        return user;
    }

    public void writeError(ErrorType errorType) throws IOException {
        byte[] buff = new byte[]{1, ResponseType.ERROR.getCode(), errorType.getCode()};
        out.write(buff);
        out.flush();
    }

    public void writeConnectedSuccessfully(Set<String> users) throws IOException {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = users.iterator();
        if (it.hasNext()) {
            for (; ; ) {
                String e = it.next();
                sb.append(e);
                if (!it.hasNext())
                    break;
                sb.append(',').append(' ');
            }
        }
        byte[] buff = sb.toString().getBytes(ENCODING);
        byte[] len = ByteBuffer.allocate(2).putShort((short) buff.length).array();
        byte[] header = new byte[]{1, ResponseType.CONNECT_SUCCESSFULLY.getCode()};
        out.write(header);
        out.write(len);
        out.write(buff);
        out.flush();
    }

    public void writeMessage(String user, byte[] message) throws IOException {
        byte[] header = new byte[]{1, ResponseType.MESSAGE.getCode()};
        out.write(header);
        byte[] buff = user.getBytes(ENCODING);
        out.write(buff.length);
        out.write(buff);
        byte[] msgbuffl = ByteBuffer.allocate(4).putInt(message.length).array();
        out.write(msgbuffl);
        out.write(message);
        out.flush();
    }

    public void writeUserConnectedOrDisconnected(ResponseType responseType, String user) throws IOException {
        byte[] buff = user.getBytes(ENCODING);
        byte[] header = new byte[]{1, responseType.getCode()};
        out.write(header);
        out.write(buff.length);
        out.write(buff);
        out.flush();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

}
