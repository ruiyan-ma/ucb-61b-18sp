package byog.Core;

import java.util.ArrayList;
import java.util.Random;

public class Hallway extends Room {

    public static final int MIN_CONNECTOR = 1;

    public static final int MAX_CONNECTOR = 2;

    /**
     * Generate a random length hallway.
     *
     * @param random:      used to generate random length.
     * @param connector:   the start connector.
     * @param boardWidth:  the width of the game board.
     * @param boardHeight: the height of the game board.
     */
    Hallway(Random random, Connector connector, int boardWidth, int boardHeight) {
        super(random, connector, boardWidth, boardHeight);
        Position pos = connector.pos;

        // compress a room into a hallway
        if (connector.dir == Direction.left) {
            top = bottom = inBoundVal(pos.y, boardHeight);
        } else if (connector.dir == Direction.right) {
            top = bottom = inBoundVal(pos.y, boardHeight);
        } else if (connector.dir == Direction.up) {
            left = right = inBoundVal(pos.x, boardWidth);
        } else {
            left = right = inBoundVal(pos.x, boardWidth);
        }

        // reset connectors using new data
        setConnectors(random, MIN_CONNECTOR, MAX_CONNECTOR, boardWidth, boardHeight);
    }
}
