package byog.Core;

import java.util.Random;

public class Hallway extends Room {

    public static final int MAX_LENGTH = 10;

    /**
     * Generate a random length hallway.
     *
     * @param random: used to generate random length.
     * @param connector: the start connector.
     * @param boardWidth: the width of the game board.
     * @param boardHeight: the height of the game board.
     */
    Hallway(Random random, Connector connector, int boardWidth, int boardHeight) {
        super();
        int length = RandomUtils.uniform(random, 5, 15);

        if (connector.dir == Direction.left) {
            right = connector.pos.x;
            left = Math.max(1, right - length);
            top = bottom = connector.pos.y;
        } else if (connector.dir == Direction.right) {
            left = connector.pos.x;
            right = Math.min(boardWidth - 2, left + length);
            top = bottom = connector.pos.y;
        } else if (connector.dir == Direction.up) {
            bottom = connector.pos.y;
            top = Math.min(boardHeight - 2, bottom + length);
            left = right = connector.pos.x;
        } else {
            top = connector.pos.y;
            bottom = Math.max(1, top - length);
            left = right = connector.pos.x;
        }
    }
}
