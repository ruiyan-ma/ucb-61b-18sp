package byog.Core;

public class Connector extends Room {

    Connector(Position pos, Direction dir) {
        super(pos);
        this.pos = pos;
        this.dir = dir;
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

}
