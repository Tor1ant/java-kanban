package service;

public enum Status {
    NEW,
    IN_PROGRESS,
    DONE;

    public String getStatus() {
        return this.name();
    }
}