package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.*;

public class Board {

    public static final double ROOM_RATIO = 0.5;

    public static final int MAX_RANDOM_TRY = 10;

    Board(Random rand, int w, int h) {
        random = rand;
        width = w;
        height = h;
        grid = new TETile[width][height];

        fillWithNothing();
        createWorld();
    }

    public TETile[][] getGrid() {
        return grid;
    }

    /**
     * Fill the board with nothing.
     */
    private void fillWithNothing() {
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                grid[i][j] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Create the world.
     */
    private void createWorld() {
        List<Room> roomList = new ArrayList<>();

        Room root = new Room(random, width, height);
        roomList.add(root);

        Queue<Connector> queue = new LinkedList<>(root.connectors);
        while (!queue.isEmpty()) {
            Connector connector = queue.poll();

            // randomly choose to generate room or hallway
            Room room;
            double ratio = RandomUtils.uniform(random);
            if (ratio <= ROOM_RATIO) {
                room = generateRoom(random, connector, roomList);
            } else {
                room = generateHallway(random, connector, roomList);
            }

            if (room != null) {
                roomList.add(connector);
                roomList.add(room);
                queue.addAll(room.connectors);
            }
        }

        // draw room
        for (Room room : roomList) {
            room.drawRoom(grid);
        }

        // draw wall
        for (Room room : roomList) {
            room.drawWall(grid);
        }
    }

    /**
     * Randomly generate a new room.
     */
    private Room generateRoom(Random random, Connector connector, List<Room> rooms) {
        int tryTime = 0;
        Room room = new Room(random, connector, width, height);

        while (room.isOverlap(rooms) && tryTime < MAX_RANDOM_TRY) {
            room = new Room(random, connector, width, height);
            tryTime += 1;
        }

        if (room.isOverlap(rooms)) {
            return null;
        }

        return room;
    }

    /**
     * Randomly generate a new hallway.
     */
    private Room generateHallway(Random random, Connector connector, List<Room> rooms) {
        int tryTime = 0;
        Room hallway = new Hallway(random, connector, width, height);

        while (hallway.isOverlap(rooms) && tryTime < MAX_RANDOM_TRY) {
            hallway = new Hallway(random, connector, width, height);
            tryTime += 1;
        }

        if (hallway.isOverlap(rooms)) {
            return null;
        }

        return hallway;
    }

    /**
     * The width of the board.
     */
    private final int width;

    /**
     * The height of the board.
     */
    private final int height;

    /**
     * The random object we will use to generate the world.
     */
    private final Random random;

    /**
     * The grid.
     */
    private final TETile[][] grid;

    public static void main(String[] args) {
        TERenderer renderer;
        renderer = new TERenderer();
        Board board = new Board(new Random(), 50, 50);
        renderer.initialize(50, 50);
        renderer.renderFrame(board.grid);
    }
}
