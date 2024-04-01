public class Shramba {
    // Singleton vzorec
    private static Shramba instance;
    public int uporabnikId = 3;

    private Shramba() {

    }

    // Singleton metoda za dostop do instance
    public static synchronized Shramba getInstance() {
        if (instance == null) {
            instance = new Shramba();
        }
        return instance;
    }
}
