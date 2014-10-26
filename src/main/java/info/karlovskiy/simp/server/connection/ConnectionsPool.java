package info.karlovskiy.simp.server.connection;

import info.karlovskiy.simp.server.ServerProperties;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 10/25/14
 */
public class ConnectionsPool {

    private final ThreadPoolExecutor threadPool;

    public ConnectionsPool() {
        int maxConnections = ServerProperties.getInstance().getMaxConnections();
        int keepAlive = ServerProperties.getInstance().getKeepAlive();
        this.threadPool = new ThreadPoolExecutor(0, maxConnections, keepAlive, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), new ConnectionsThreadFactory());
    }

    static class ConnectionsThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final ThreadGroup group;

        ConnectionsThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
        }

        @Override
        public Thread newThread(Runnable task) {
            Thread t = new Thread(group, task,
                    "SIMP connection " + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }

    public void add(Runnable task) {
        threadPool.execute(task);
    }

}
