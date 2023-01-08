package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Room class.
 *
 * @author ruiyan ma
 */
public class Room {

    public static final int MIN_LENGTH = 5;

    public static final int MAX_LENGTH = 15;

    public static final int MIN_CONNECTOR = 3;

    public static final int MAX_CONNECTOR = 6;

    /**
     * Generate a room for the given position.
     */
    Room(Position pos) {
        left = right = pos.x;
        top = bottom = pos.y;
    }

    /**
     * Generate a random sized room.
     * First randomly generate a center point, then generate the width and the height.
     * All rooms should be at least 1 space away from the bound of the game board.
     *
     * @param random:      used to generate random size.
     * @param boardWidth:  the width of the game board.
     * @param boardHeight: the height of the game board.
     */
    Room(Random random, int boardWidth, int boardHeight) {
        Position center = new Position(
                RandomUtils.uniform(random, 2, boardWidth - 1),
                RandomUtils.uniform(random, 2, boardHeight - 1));
        int width = RandomUtils.uniform(random, MIN_LENGTH, MAX_LENGTH + 1);
        int height = RandomUtils.uniform(random, MIN_LENGTH, MAX_LENGTH + 1);

        left = inBoundVal(center.x - width / 2, boardWidth);
        right = inBoundVal(center.x + width / 2, boardWidth);
        bottom = inBoundVal(center.y - height / 2, boardHeight);
        top = inBoundVal(center.y + height / 2, boardHeight);

        setConnectors(random, MIN_CONNECTOR, MAX_CONNECTOR, boardWidth, boardHeight);
    }

    /**
     * Generate a random sized room with the given connector.
     * The new generated room should not overlap with the given connector.
     * All rooms should be at least 1 space away from the bound of the game board.
     *
     * @param random:      used to generate random size.
     * @param connector:   the given connector.
     * @param boardWidth:  the width of the game board.
     * @param boardHeight: the height of the game board.
     */
    Room(Random random, Connector connector, int boardWidth, int boardHeight) {
        Position pos = connector.pos;
        Direction dir = connector.dir;

        int width = RandomUtils.uniform(random, MIN_LENGTH, MAX_LENGTH + 1);
        int height = RandomUtils.uniform(random, MIN_LENGTH, MAX_LENGTH + 1);

        if (dir == Direction.left || dir == Direction.right) {
            if (dir == Direction.left) {
                right = inBoundVal(pos.x - 1, boardWidth);
                left = inBoundVal(right - width, boardWidth);
            } else {
                left = inBoundVal(pos.x + 1, boardWidth);
                right = inBoundVal(left + width, boardWidth);
            }

            int offset = RandomUtils.uniform(random, height);
            bottom = inBoundVal(pos.y - offset, boardHeight);
            top = inBoundVal(bottom + height, boardHeight);
        } else {
            if (dir == Direction.up) {
                bottom = inBoundVal(pos.y + 1, boardHeight);
                top = inBoundVal(bottom + height, boardHeight);
            } else {
                top = inBoundVal(pos.y - 1, boardHeight);
                bottom = inBoundVal(top - height, boardHeight);
            }

            int offset = RandomUtils.uniform(random, width);
            left = inBoundVal(pos.x - offset, boardWidth);
            right = inBoundVal(left + width, boardWidth);
        }

        setConnectors(random, MIN_CONNECTOR, MAX_CONNECTOR, boardWidth, boardHeight);
    }

    /**
     * Check whether the given value is in bound.
     */
    protected boolean inBound(int value, int bound) {
        return value >= 1 && value <= bound - 2;
    }

    /**
     * Convert the given value into a inbound value.
     *
     * @param val:   the given value
     * @param bound: the bound
     */
    protected int inBoundVal(int val, int bound) {
        val = Math.max(1, val);
        val = Math.min(val, bound - 2);
        return val;
    }

    /**
     * Check overlap between this room and another room.
     * <p>
     * Not overlap room should meet one of the following:
     * 1. room.left > this.right
     * 2. room.right < this.left
     * 3. room.bottom > this.top
     * 4. room.top < this.bottom
     *
     * @param room: the other room
     */
    private boolean isOverlap(Room room) {
        return room.left <= right
                && room.right >= left
                && room.bottom <= top
                && room.top >= bottom;
    }

    /**
     * Check overlap between this room and any room in the given list.
     */
    public boolean isOverlap(List<Room> rooms) {
        for (Room room : rooms) {
            if (isOverlap(room)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generate connectors for this room.
     * All connectors should not overlap with the current room.
     */
    protected void setConnectors(Random random, int minNum, int maxNum, int boardWidth, int boardHeight) {
        int num = RandomUtils.uniform(random, minNum, maxNum + 1);
        connectors = new ArrayList<>(num);

        for (int i = 0; i < num; ++i) {
            Direction dir = Direction.intToDirection(i);

            int x, y;
            if (dir == Direction.left) {
                x = left - 1;
                y = RandomUtils.uniform(random, bottom, top + 1);
            } else if (dir == Direction.right) {
                x = right + 1;
                y = RandomUtils.uniform(random, bottom, top + 1);
            } else if (dir == Direction.up) {
                y = top + 1;
                x = RandomUtils.uniform(random, left, right + 1);
            } else {
                y = bottom - 1;
                x = RandomUtils.uniform(random, left, right + 1);
            }

            if (inBound(x, boardWidth) && inBound(y, boardHeight)) {
                connectors.add(new Connector(new Position(x, y), dir));
            }
        }
    }

    /**
     * Draw this room on the given board.
     *
     * @param world: the game board.
     */
    public void drawRoom(TETile[][] world) {
        for (int i = left; i <= right; ++i) {
            for (int j = bottom; j <= top; ++j) {
                world[i][j] = Tileset.FLOOR;
            }
        }
    }

    /**
     * Draw wall for this room.
     *
     * @param world: the game board.
     */
    public void drawWall(TETile[][] world) {
        // top and bottom
        for (int i = left - 1; i <= right + 1; ++i) {
            if (world[i][top + 1] == Tileset.NOTHING) {
                world[i][top + 1] = Tileset.WALL;
            }

            if (world[i][bottom - 1] == Tileset.NOTHING) {
                world[i][bottom - 1] = Tileset.WALL;
            }
        }

        // left and right
        for (int j = bottom - 1; j <= top + 1; ++j) {
            if (world[left - 1][j] == Tileset.NOTHING) {
                world[left - 1][j] = Tileset.WALL;
            }

            if (world[right + 1][j] == Tileset.NOTHING) {
                world[right + 1][j] = Tileset.WALL;
            }
        }
    }

    @Override
    public String toString() {
        return "left = " + left + '\n' +
                "right = " + right + '\n' +
                "top = " + top + '\n' +
                "bottom = " + bottom + '\n' +
                "width = " + (right - left) + '\n' +
                "height= " + (top - bottom);
    }

    /**
     * The left bound of this room.
     */
    int left;

    /**
     * The right bound of this room.
     */
    int right;

    /**
     * The bottom bound of this room.
     */
    int bottom;

    /**
     * The top bound of this room.
     */
    int top;

    /**
     * Connector list.
     */
    List<Connector> connectors;
}
