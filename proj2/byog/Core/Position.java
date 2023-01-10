package byog.Core;

import java.io.Serializable;

/**
 * Position class, used to represent a coordinate.
 *
 * @author ruiyan ma
 */
public class Position implements Serializable {

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