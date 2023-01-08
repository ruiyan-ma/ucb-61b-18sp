package byog.Core;

public class Connector {

    Connector(Position pos, Direction dir) {
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
