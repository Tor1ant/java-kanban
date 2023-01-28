package service;

public class ValidateException extends RuntimeException {
    private final String cause;

    public ValidateException(String message, String cause) {
        super(message);
        this.cause = cause;
    }

    public String getMessage() {
        return String.format("Задача " + "\"%s\"" + " пересекается по времени с существующей задачей " + "\"%s\"",
                super.getMessage(), this.cause);
    }
}