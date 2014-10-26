package info.karlovskiy.simp.server.request;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 10/26/14
 */
public enum RequestType {

    CONNECT(0),
    DISCONNECT(1),
    MESSAGE(2);

    private byte code;

    private RequestType(int code) {
        this.code = (byte) code;
    }

    public byte getCode() {
        return code;
    }

    public static RequestType valueOf(byte code) {
        for (RequestType requestType : values()) {
            if (requestType.getCode() == code) {
                return requestType;
            }
        }
        return null;
    }
}
