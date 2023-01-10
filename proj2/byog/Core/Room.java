package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static byog.Core.Game.*;

/**
 * Room class.
 *
 * @author ruiyan ma
 */
public class Room {

    public static final int MIN_LENGTH = 3;

    public static final int MAX_LENGTH = 10;

    public static final int ROOM_TYPE = 0;

    public static final int HALLWAY_TYPE = 1;

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
     * All rooms should be at least MARGIN space away from the bound of the game board.
     *
     * @param random: used to generate random size.
     */
    Room(Random random) {
        Position center = new Position(
                RandomUtils.uniform(random, MARGIN, WIDTH - MARGIN),
                RandomUtils.uniform(random, MARGIN, HEIGHT - MARGIN));
        int width = RandomUtils.uniform(random, MIN_LENGTH, MAX_LENGTH + 1);
        int height = RandomUtils.uniform(random, MIN_LENGTH, MAX_LENGTH + 1);

        left = inBoundVal(center.x - width / 2, WIDTH);
        right = inBoundVal(center.x + width / 2, WIDTH);
        bottom = inBoundVal(center.y - height / 2, HEIGHT);
        top = inBoundVal(center.y + height / 2, HEIGHT);

        setConnectors(random);
    }

    /**
     * Generate a random sized room with the given connector.
     * All rooms should be at least MARGIN space away from the bound of the game board.
     *
     * @param random:    used to generate random size.
     * @param connector: the given connector.
     */
    Room(Random random, Connector connector) {
        Position pos = connector.pos;
        Direction dir = connector.dir;

        int width = RandomUtils.uniform(random, MIN_LENGTH, MAX_LENGTH + 1);
        int height = RandomUtils.uniform(random, MIN_LENGTH, MAX_LENGTH + 1);

        if (dir == Direction.left || dir == Direction.right) {
            if (dir == Direction.left) {
                right = inBoundVal(pos.x, WIDTH);
                left = inBoundVal(right - width, WIDTH);
            } else {
                left = inBoundVal(pos.x, WIDTH);
                right = inBoundVal(left + width, WIDTH);
            }

            int offset = RandomUtils.uniform(random, height);
            bottom = inBoundVal(pos.y - offset, HEIGHT);
            top = inBoundVal(bottom + height, HEIGHT);
        } else {
            if (dir == Direction.up) {
                bottom = inBoundVal(pos.y, HEIGHT);
                top = inBoundVal(bottom + height, HEIGHT);
            } else {
                top = inBoundVal(pos.y, HEIGHT);
                bottom = inBoundVal(top - height, HEIGHT);
            }

            int offset = RandomUtils.uniform(random, width);
            left = inBoundVal(pos.x - offset, WIDTH);
            right = inBoundVal(left + width, WIDTH);
        }

        setConnectors(random);
    }

    /**
     * Check whether the given value is in bound.
     */
    protected boolean inBound(int value, int bound) {
        return value >= MARGIN && value <= bound - MARGIN;
    }

    /**
     * Convert the given value into a inbound value.
     *
     * @param val:   the given value
     * @param bound: the bound
     */
    protected int inBoundVal(int val, int bound) {
        val = Math.max(MARGIN, val);
        val = Math.min(val, bound - MARGIN);
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
    protected void setConnectors(Random random) {
        connectors = new ArrayList<>();

        for (Direction dir : Direction.values()) {
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

            if (inBound(x, WIDTH) && inBound(y, HEIGHT)) {
                connectors.add(new Connector(new Position(x, y), dir, HALLWAY_TYPE));
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
