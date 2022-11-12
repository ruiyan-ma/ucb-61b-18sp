package byog.Core;

/**
 * Position class, used to represent a coordinate.
 *
 * @author ruiyan ma
 */
public class Position {

    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "Position(" + x + ", " + y + ")";
    }

    int x;

    int y;
}