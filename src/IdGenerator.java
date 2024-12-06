public class IdGenerator {

    private static Integer identificatorID = 0;

    public IdGenerator() {
    }

    public Integer getIdentificatorID() {
        identificatorID++;
        return identificatorID;
    }
}