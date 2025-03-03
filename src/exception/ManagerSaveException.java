package exception;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(String errorText) {
        super(errorText);
    }
}
