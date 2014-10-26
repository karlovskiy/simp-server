package info.karlovskiy.simp.server;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 10/26/14
 */
public class ServerProperties {

    public static final String ENCODING = "UTF-8";

    private static final String SERVER_PORT = "server_port";
    private static final String MAX_CONNECTIONS = "max_connections";
    private static final String CONNECTION_KEEP_ALIVE = "connection_keep_alive";

    private Properties properties = new Properties();


    public static ServerProperties getInstance() {
        return ServerPropertiesHolder.INSTANCE;
    }

    public int getPort() {
        return Integer.parseInt(properties.getProperty(SERVER_PORT, "36484"));
    }

    public int getMaxConnections() {
        return Integer.valueOf(properties.getProperty(MAX_CONNECTIONS, "64"));
    }

    public int getKeepAlive() {
        return Integer.valueOf(properties.getProperty(CONNECTION_KEEP_ALIVE, "60"));
    }

    private static class ServerPropertiesHolder {
        private static final ServerProperties INSTANCE = new ServerProperties();
    }

    private ServerProperties() {
        try {
            properties.load(new FileInputStream("server.properties"));
        } catch (Throwable ignored) {
        }
    }


}
