package service;

public class ValidateException extends RuntimeException {
    public ValidateException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}