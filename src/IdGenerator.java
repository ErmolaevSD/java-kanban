public class IdGenerator {

    private static int identificatorID = 0;

    public IdGenerator() {
    }
    public int getIdentificatorID() {
        identificatorID++;
        return identificatorID;
    }
}
