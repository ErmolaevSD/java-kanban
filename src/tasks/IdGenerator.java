package tasks;

public class IdGenerator {

    private static Integer identificationID = 0;

    public IdGenerator() {
    }

    public Integer getIdentificationID() {
        return identificationID++;
    }
}