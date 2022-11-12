package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.*;

public class Board {

    public static final int WIDTH = 50;

    public static final int HEIGHT = 50;

    public static final int MIN_ROOM_NUM = 40;

    public static final int MAX_ROOM_NUM = 100;

    public static final double ROOM_RATIO = 0.4;

    public static final int MAX_RANDOM_TRY = 20;

    Board(Random rand) {
        random = rand;
        grid = new TETile[WIDTH][HEIGHT];
    }

    /**
     * Fill the board with nothing.
     */
    private void fillWithNothing() {
        for (int i = 0; i < WIDTH; ++i) {
            for (int j = 0; j < HEIGHT; ++j) {
                grid[i][j] = Tileset.NOTHING;
            }
        }
    }

//    private void debugRender(Room room) {
//        room.drawRoom(grid);
//        renderer.renderFrame(grid);
//        renderer.renderFrame(grid);
//    }

    /**
     * Create the world.
     */
    public void createWorld() {
        int roomNum = RandomUtils.uniform(random, MIN_ROOM_NUM, MAX_ROOM_NUM);
        List<Room> roomList = new ArrayList<>(roomNum);

        Room root = new Room(random, WIDTH, HEIGHT);
        roomList.add(root);
//        debugRender(root);

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
                Room connectSpace = new Room(connector);

//                debugRender(connectSpace);
//                debugRender(room);

                roomList.add(room);
                roomList.add(connectSpace);
                if (roomList.size() < roomNum) {
                    queue.addAll(room.connectors);
                }
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

//        System.out.println("room number: " + roomList.size());
    }

    /**
     * Randomly generate a new room.
     */
    private Room generateRoom(Random random, Connector connector, List<Room> rooms) {
        int tryTime = 0;
        Room room = new Room(random, connector, WIDTH, HEIGHT);

        while (room.isOverlap(rooms) && tryTime < MAX_RANDOM_TRY) {
            room = new Room(random, connector, WIDTH, HEIGHT);
            tryTime += 1;
        }

        if (tryTime < MAX_RANDOM_TRY) {
            return room;
        } else {
            return null;
        }
    }

    /**
     * Randomly generate a new hallway.
     */
    private Room generateHallway(Random random, Connector connector, List<Room> rooms) {
        int tryTime = 0;
        Room hallway = new Hallway(random, connector, WIDTH, HEIGHT);

        while (hallway.isOverlap(rooms) && tryTime < MAX_RANDOM_TRY) {
            hallway = new Hallway(random, connector, WIDTH, HEIGHT);
            tryTime += 1;
        }

        if (tryTime < MAX_RANDOM_TRY) {
            return hallway;
        } else {
            return null;
        }
    }


    Random random;

    /**
     * The grid.
     */
    TETile[][] grid;

    public static TERenderer renderer;

    public static void main(String[] args) {
        renderer = new TERenderer();
        Board board = new Board(new Random());
        renderer.initialize(WIDTH, HEIGHT);

        board.fillWithNothing();

        board.createWorld();
        renderer.renderFrame(board.grid);
    }
}
