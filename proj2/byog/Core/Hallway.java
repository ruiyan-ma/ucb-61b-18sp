package byog.Core;

import java.util.Random;

public class Hallway extends Room {

    public static final int MIN_LENGTH = 5;

    public static final int MAX_LENGTH = 15;

    public static final int MAX_HALLWAY = 2;

    /**
     * Generate a random length hallway.
     *
     * @param random:      used to generate random length.
     * @param start:       the start connector.
     * @param boardWidth:  the width of the game board.
     * @param boardHeight: the height of the game board.
     */
    Hallway(Random random, Connector start, int boardWidth, int boardHeight) {
        super();
        dir = start.dir;
        startPos = start.pos;
        int length = RandomUtils.uniform(random, MIN_LENGTH, MAX_LENGTH);

        if (start.dir == Direction.left) {
            right = start.pos.x;
            left = Math.max(1, right - length);
            top = bottom = start.pos.y;
            endPos = new Position(left, top);
        } else if (start.dir == Direction.right) {
            left = start.pos.x;
            right = Math.min(boardWidth - 2, left + length);
            top = bottom = start.pos.y;
            endPos = new Position(right, top);
        } else if (start.dir == Direction.up) {
            bottom = start.pos.y;
            top = Math.min(boardHeight - 2, bottom + length);
            left = right = start.pos.x;
            endPos = new Position(left, top);
        } else {
            top = start.pos.y;
            bottom = Math.max(1, top - length);
            left = right = start.pos.x;
            endPos = new Position(left, bottom);
        }
    }

    @Override
    public Connector connector(Random random) {
        Direction endDir = Direction.randomDir(random);
        while (endDir == Direction.reverse(dir)) {
            endDir = Direction.randomDir(random);
        }
        return new Connector(endPos, endDir);
    }

    @Override
    public int maxHallway() {
        return MAX_HALLWAY;
    }

    /**
     * Start position.
     */
    Position startPos;

    /**
     * End position.
     */
    Position endPos;

    /**
     * Growth direction.
     */
    Direction dir;
}
