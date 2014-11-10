package info.karlovskiy.simp.server.jmx;

import info.karlovskiy.simp.server.Server;
import info.karlovskiy.simp.server.connection.Connection;
import info.karlovskiy.simp.server.connection.ConnectionsPool;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 11/9/14
 */
public class Administration implements ServerAdministration {

    private ConnectionsPool connectionsPool;

    public static Administration getInstance() {
        return AdministrationHolder.INSTANCE;
    }

    public void setConnectionsPool(ConnectionsPool connectionsPool) {
        this.connectionsPool = connectionsPool;
    }

    @Override
    public int getMaxConnections() {
        return connectionsPool.getMaxConnections();
    }

    @Override
    public void setMaxConnections(int maxConnections) {
        connectionsPool.setMaxConnections(maxConnections);
    }

    @Override
    public int getAlwaysAliveConnections() {
        return connectionsPool.getAlwaysAliveConnections();
    }

    @Override
    public void setAlwaysAliveConnections(int alwaysAliveConnections) {
        connectionsPool.setAlwaysAliveConnections(alwaysAliveConnections);
    }

    @Override
    public long getInactiveConnectionKeepAliveSeconds() {
        return connectionsPool.getInactiveConnectionKeepAliveSeconds();
    }

    @Override
    public void setInactiveConnectionKeepAliveSeconds(long inactiveConnectionKeepAliveSeconds) {
        connectionsPool.setInactiveConnectionKeepAliveSeconds(inactiveConnectionKeepAliveSeconds);
    }

    @Override
    public int getOnlineUsersCount() {
        return connectionsPool.getOnlineUsersCount();
    }

    @Override
    public Set<String> getOnlineUsers() {
        return Server.getConnectedUsers().keySet();
    }

    @Override
    public void dropUser(String name) {
        ConcurrentHashMap<String, Connection> connectedUsers = Server.getConnectedUsers();
        Connection connection = connectedUsers.get(name);
        try {
            connection.close();
        } catch (IOException e) {
            throw new RuntimeException("Error dropping user " + name);
        }
    }

    private static class AdministrationHolder {
        private static final Administration INSTANCE = new Administration();
    }

    private Administration() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("info.karlovskiy.simp.server.jmx:type=ServerAdministration");
            mbs.registerMBean(this, name);
        } catch (Exception e) {
            throw new RuntimeException("Error initialising jmx administration", e);
        }
    }
}
