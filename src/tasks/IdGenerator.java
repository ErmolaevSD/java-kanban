package tasks;

public class IdGenerator {

    private static Integer identificationID = 0;

    public IdGenerator() {
    }

    public Integer getIdentificationID() {
        identificationID++;
        return identificationID;
    }

    public void setIdentificationID(Integer identificationID) {
        IdGenerator.identificationID = identificationID;
    }
}