package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.List;
import java.util.Random;

/**
 * Room class.
 */
public class Room {

    public static final int MAX_WIDTH = 15;

    public static final int MAX_HEIGHT = 15;

    /**
     * Empty constructor for subclass.
     */
    Room() {
    }

    /**
     * Generate a random sized room.
     * <p>
     * All rooms should be at least 1 space away from the bound of
     * the game board.
     *
     * @param random:      used to generate random size.
     * @param boardWidth:  the width of the game board.
     * @param boardHeight: the height of the game board.
     */
    Room(Random random, int boardWidth, int boardHeight) {
        Position center = new Position(
                RandomUtils.uniform(random, 1, boardWidth - 1),
                RandomUtils.uniform(random, 1, boardHeight - 1));
        int width = random.nextInt(MAX_WIDTH);
        int height = random.nextInt(MAX_HEIGHT);
        left = Math.max(1, center.x - width / 2);
        right = Math.min(boardWidth - 2, center.x + width / 2);
        bottom = Math.max(1, center.y - height / 2);
        top = Math.min(boardHeight - 2, center.y + height / 2);
    }

    /**
     * Generate a random sized room.
     * <p>
     * All rooms should be at least 1 space away from the bound of
     * the game board.
     *
     * @param random:      used to generate random size.
     * @param center:      the center position of this room.
     * @param boardWidth:  the width of the game board.
     * @param boardHeight: the height of the game board.
     */
    Room(Random random, Position center, int boardWidth, int boardHeight) {
        int width = random.nextInt(MAX_WIDTH);
        int height = random.nextInt(MAX_HEIGHT);
        left = Math.max(1, center.x - width / 2);
        right = Math.min(boardWidth - 2, center.x + width / 2);
        bottom = Math.max(1, center.y - height / 2);
        top = Math.min(boardHeight - 2, center.y + height / 2);
    }

    /**
     * Check overlap between this room and another room.
     * <p>
     * Not overlap room should meet one of the following:
     * 1. room.left - 1 > this.right + 1
     * 2. room.right + 1 < this.left - 1
     * 3. room.bottom - 1 > this.top + 1
     * 4. room.top + 1 < this.bottom - 1
     * Leave 2 spaces for walls
     *
     * @param room: the other room
     */
    private boolean isOverlap(Room room) {
        return room.left <= right + 2
                && room.right >= left - 2
                && room.bottom <= top + 2
                && room.top >= bottom - 2;
    }

    /**
     * Check overlap between this room and any room in the given list.
     */
    public boolean noOverlap(List<Room> rooms) {
        for (Room room : rooms) {
            if (isOverlap(room)) {
                return false;
            }
        }
        return true;
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
     * Generate a random connector.
     * */
    public Connector connector(Random random) {
        return new Connector(random, this);
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
}
