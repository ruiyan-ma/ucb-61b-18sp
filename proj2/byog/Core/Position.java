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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Position)) {
            return false;
        }

        Position pos = (Position) obj;
        return pos.x == x && pos.y == y;
    }

    public String toString() {
        return "Position(" + x + ", " + y + ")";
    }

    int x;

    int y;
}