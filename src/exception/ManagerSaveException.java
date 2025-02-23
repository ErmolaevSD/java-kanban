package exception;

public class ManagerSaveException extends RuntimeException{

    private String errorText;

    public ManagerSaveException(String errorText) {
        this.errorText = errorText;
    }
}
