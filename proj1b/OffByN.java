public class OffByN implements CharacterComparator {

    /**
     * Constructor
     */
    public OffByN(int N) {
        this.N = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == N;
    }

    private int N;
}
