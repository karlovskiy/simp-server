package info.karlovskiy.simp.server;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 10/26/14
 */
public class ServerProperties {

    private static final Logger logger = Logger.getLogger(ServerProperties.class.getName());

    public static final String ENCODING = "UTF-8";

    private static final String PROPERTIES_FILE = "server.properties";
    private static final String SERVER_PORT = "server_port";
    private static final String MAX_CONNECTIONS = "max_connections";
    private static final String CONNECTION_KEEP_ALIVE = "connection_keep_alive";

    private Properties properties = new Properties();


    public static ServerProperties getInstance() {
        return ServerPropertiesHolder.INSTANCE;
    }

    public int getPort() {
        return Integer.parseInt(properties.getProperty(SERVER_PORT, "7777"));
    }

    public int getMaxConnections() {
        return Integer.parseInt(properties.getProperty(MAX_CONNECTIONS, "64"));
    }

    public int getKeepAlive() {
        return Integer.parseInt(properties.getProperty(CONNECTION_KEEP_ALIVE, "30"));
    }

    private static class ServerPropertiesHolder {
        private static final ServerProperties INSTANCE = new ServerProperties();
    }

    private ServerProperties() {
        String propertiesFile = System.getProperty("simp.home") + File.separator + PROPERTIES_FILE;
        try {
            properties.load(new FileInputStream(propertiesFile));
            logger.info("Properties from " + propertiesFile + " successfully loaded.");
        } catch (Throwable ignored) {
            logger.warning("Properties file " + propertiesFile + " not found. Using default properties.");
        }
    }


}
