package info.karlovskiy.simp.server;

import info.karlovskiy.simp.server.connection.Connection;
import info.karlovskiy.simp.server.connection.ConnectionThread;
import info.karlovskiy.simp.server.connection.ConnectionsPool;
import info.karlovskiy.simp.server.jmx.Administration;
import info.karlovskiy.simp.server.response.ErrorType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 10/25/14
 */
public class Server {

    private static final ConcurrentHashMap<String, Connection> users2Connections = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) {

        ConnectionsPool connectionsPool = new ConnectionsPool();
        Administration administration = Administration.getInstance();
        administration.setConnectionsPool(connectionsPool);
        int port = ServerProperties.getInstance().getPort();
        try (ServerSocket s = new ServerSocket(port)) {
            logger.info("Simp server started on port " + port);
            while (true) {
                Socket socket = s.accept();
                try {
                    logger.info("Creating new connection from " + socket.getRemoteSocketAddress());
                    Connection connection = new Connection(socket);
                    ConnectionThread thread = new ConnectionThread(connection);
                    try {
                        connectionsPool.add(thread);
                    } catch (RejectedExecutionException ree) {
                        connection.writeError(ErrorType.SERVER_UNAVAILABLE);
                        logger.severe("New connection rejected: max connections reached");
                        connection.close();
                    }
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error while creating new connection", e);
                    socket.close();
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Server error ", e);
        }

    }

    public static ConcurrentHashMap<String, Connection> getConnectedUsers() {
        return users2Connections;
    }

}
