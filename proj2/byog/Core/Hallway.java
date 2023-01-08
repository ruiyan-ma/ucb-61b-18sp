package byog.Core;

import java.util.Random;

public class Hallway extends Room {

    public static final int MIN_CONNECTOR = 2;

    public static final int MAX_CONNECTOR = 4;

    /**
     * Generate a random sized hallway.
     *
     * @param random:      used to generate random length.
     * @param connector:   the start connector.
     * @param boardWidth:  the width of the game board.
     * @param boardHeight: the height of the game board.
     */
    Hallway(Random random, Connector connector, int boardWidth, int boardHeight) {
        super(connector.pos);
        Position pos = connector.pos;
        Direction dir = connector.dir;

        int size = RandomUtils.uniform(random, MIN_LENGTH, MAX_LENGTH + 1);
        if (dir == Direction.left || dir == Direction.right) {
            if (dir == Direction.left) {
                right = inBoundVal(pos.x - 1, boardWidth);
                left = inBoundVal(right - size, boardWidth);
            } else {
                left = inBoundVal(pos.x + 1, boardWidth);
                right = inBoundVal(left + size, boardWidth);
            }
        } else {
            if (dir == Direction.up) {
                bottom = inBoundVal(pos.y + 1, boardHeight);
                top = inBoundVal(bottom + size, boardHeight);
            } else {
                top = inBoundVal(pos.y - 1, boardHeight);
                bottom = inBoundVal(top - size, boardHeight);
            }
        }

        setConnectors(random, MIN_CONNECTOR, MAX_CONNECTOR, boardWidth, boardHeight);
    }
}
