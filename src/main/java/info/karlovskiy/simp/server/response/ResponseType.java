package info.karlovskiy.simp.server.response;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 10/26/14
 */
public enum ResponseType {

    ERROR(0),
    CONNECT_SUCCESSFULLY(1),
    USER_CONNECTED(2),
    USER_DISCONNECTED(3),
    MESSAGE(4);

    private byte code;

    private ResponseType(int code) {
        this.code = (byte) code;
    }

    public byte getCode() {
        return code;
    }

    public static ResponseType valueOf(byte code) {
        for (ResponseType responseType : values()) {
            if (responseType.getCode() == code) {
                return responseType;
            }
        }
        return null;
    }
}
