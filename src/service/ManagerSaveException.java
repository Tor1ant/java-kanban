package service;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message) {
        super(message);
    }


    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}
