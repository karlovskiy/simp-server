package info.karlovskiy.simp.server.connection;

import info.karlovskiy.simp.server.response.ErrorType;
import info.karlovskiy.simp.server.request.RequestType;
import info.karlovskiy.simp.server.response.ResponseType;
import info.karlovskiy.simp.server.Server;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 10/25/14
 */
public class ConnectionThread implements Runnable {

    private static final Logger logger = Logger.getLogger(ConnectionThread.class.getName());

    private final Connection connection;
    private ThreadLocal<String> connectionUser = new ThreadLocal<>();

    public ConnectionThread(Connection connection) throws IOException {
        this.connection = connection;
    }

    @Override
    public void run() {
        ConcurrentHashMap<String, Connection> connectedUsers = Server.getConnectedUsers();
        try {
            String user = connection.readUserConnected();
            Connection existsConnection = connectedUsers.putIfAbsent(user, connection);
            if (existsConnection != null) {
                connection.writeError(ErrorType.USERNAME_ALREADY_EXISTS);
                logger.severe("New connection error: user " + user + " already exists");
                connection.close();
                return;
            }
            connectionUser.set(user);
            connection.writeConnectedSuccessfully(connectedUsers.keySet());
            for (Connection conn : connectedUsers.values()) {
                conn.writeUserConnectedOrDisconnected(ResponseType.USER_CONNECTED, user);
            }
            logger.info("User " + user + " successfully connected.");
            while (true) {
                byte[] header = connection.readHeader();
                RequestType requestType = RequestType.valueOf(header[1]);
                if (requestType == RequestType.DISCONNECT) {
                    break;
                } else if (requestType == RequestType.MESSAGE) {
                    String messageUser = connection.readUser();
                    byte[] message = connection.readMessage();
                    for (Connection conn : connectedUsers.values()) {
                        conn.writeMessage(messageUser, message);
                    }
                }
            }
        } catch (Throwable e) {
            logger.log(Level.SEVERE, "Connection error", e);
        } finally {
            String user = connectionUser.get();
            if (user != null) {
                logger.info("Disconnecting user " + user);
                try {
                    connectedUsers.remove(user);
                    for (Connection conn : connectedUsers.values()) {
                        conn.writeUserConnectedOrDisconnected(ResponseType.USER_DISCONNECTED, user);
                    }
                } catch (Throwable e) {
                    logger.log(Level.SEVERE, "Error disconnecting user " + user, e);
                }
            }
            try {
                connection.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error closing connection", e);
            }
        }
    }
}
