package info.karlovskiy.simp.server.response;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 10/26/14
 */
public enum ErrorType {

    SERVER_UNAVAILABLE(0),
    USERNAME_ALREADY_EXISTS(1);

    private byte code;

    private ErrorType(int code) {
        this.code = (byte) code;
    }

    public byte getCode() {
        return code;
    }
}
