public class IdGenerator {

    private static Integer identificatorID = (Integer) 0;

    public IdGenerator() {
    }
    public int getIdentificatorID() {
        identificatorID++;
        return identificatorID;
    }
}
