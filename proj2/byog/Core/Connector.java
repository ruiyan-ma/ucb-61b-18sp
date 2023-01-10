package byog.Core;

public class Connector {

    Connector(Position pos, Direction dir, int type) {
        this.pos = pos;
        this.dir = dir;
        this.nextType = type;
    }

    public String toString() {
        return "Connector(" + pos.toString() + ", " + dir + ")\n";
    }

    /**
     * The position of this connector.
     */
    Position pos;

    /**
     * The growth direction of this connector.
     */
    Direction dir;

    /**
     * The type of the next room.
     */
    int nextType;
}
