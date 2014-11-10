package info.karlovskiy.simp.server;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 11/3/14
 */
public class ServerUtils {

    public static byte[] toBytesArray(int i) {
        return new byte[]{
                (byte) ((i >> 24) & 0xFF),
                (byte) ((i >> 16) & 0xFF),
                (byte) ((i >> 8) & 0xFF),
                (byte) (i & 0xFF)
        };
    }

    public static int toInt(byte[] b) {
        return (b[0] & 0xFF) << 24 |
                (b[1] & 0xFF) << 16 |
                (b[2] & 0xFF) << 8 |
                (b[3] & 0xFF);
    }

    public static byte[] toBytesArray(short s) {
        return new byte[]{
                (byte) ((s >> 8) & 0xFF),
                (byte) (s & 0xFF)
        };
    }

    public static int toShort(byte[] b) {
        return (b[0] & 0xFF) << 8 |
                (b[1] & 0xFF);
    }

    private ServerUtils() {
    }
}
