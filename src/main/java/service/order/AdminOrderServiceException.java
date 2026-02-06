package service.order;

public class AdminOrderServiceException extends Exception {

    public AdminOrderServiceException(String message) {
        super(message);
    }

    public AdminOrderServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
