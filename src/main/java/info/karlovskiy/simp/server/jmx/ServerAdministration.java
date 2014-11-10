package info.karlovskiy.simp.server.jmx;

import javax.management.MXBean;
import java.util.Set;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 11/6/14
 */
@MXBean
public interface ServerAdministration {

    public int getMaxConnections();

    public void setMaxConnections(int maxConnections);

    public int getAlwaysAliveConnections();

    public void setAlwaysAliveConnections(int alwaysAliveConnections);

    public long getInactiveConnectionKeepAliveSeconds();

    public void setInactiveConnectionKeepAliveSeconds(long inactiveConnectionKeepAliveSeconds);

    public int getOnlineUsersCount();

    public Set<String> getOnlineUsers();

    public void dropUser(String name);

}
