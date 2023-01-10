package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.*;

import static byog.Core.Game.HEIGHT;
import static byog.Core.Game.WIDTH;
import static byog.Core.Room.ROOM_TYPE;

public class World {

    public static final int MAX_RANDOM_TRY = 5;

    World(long seed) {
        random = new Random(seed);
        board = new TETile[WIDTH][HEIGHT];

        fillWithNothing();
        createWorld();
        generatePlayer();
    }

    /**
     * Fill the board with nothing.
     */
    private void fillWithNothing() {
        for (int i = 0; i < WIDTH; ++i) {
            for (int j = 0; j < HEIGHT; ++j) {
                board[i][j] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Create the world.
     */
    private void createWorld() {
        List<Room> roomList = new ArrayList<>();

        Room root = new Room(random);
        roomList.add(root);

        Queue<Connector> connectors = new LinkedList<>(root.connectors);
        while (!connectors.isEmpty()) {
            Connector connector = connectors.poll();

            Room room;
            if (connector.nextType == ROOM_TYPE) {
                room = generateRoom(random, connector, roomList);
            } else {
                room = generateHallway(random, connector, roomList);
            }

            if (room != null) {
                roomList.add(room);
                connectors.addAll(room.connectors);
            }
        }

        // draw room
        for (Room room : roomList) {
            room.drawRoom(board);
        }

        // draw wall
        for (Room room : roomList) {
            room.drawWall(board);
        }
    }

    /**
     * Randomly generate a new room.
     */
    private Room generateRoom(Random random, Connector connector, List<Room> rooms) {
        int tryTime = 0;
        Room room = new Room(random, connector);

        while (room.isOverlap(rooms) && tryTime < MAX_RANDOM_TRY) {
            room = new Room(random, connector);
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
        Room hallway = new Hallway(random, connector);

        while (hallway.isOverlap(rooms) && tryTime < MAX_RANDOM_TRY) {
            hallway = new Hallway(random, connector);
            tryTime += 1;
        }

        if (hallway.isOverlap(rooms)) {
            return null;
        }

        return hallway;
    }

    /**
     * Generate player.
     */
    private void generatePlayer() {
        Position pos = new Position(
                random.nextInt(WIDTH), random.nextInt(HEIGHT));
        while (!isFloor(pos)) {
            pos.x = random.nextInt(WIDTH);
            pos.y = random.nextInt(HEIGHT);
        }

        player = pos;
        board[pos.x][pos.y] = Tileset.PLAYER;
    }

    /**
     * Move the player according to the given key.
     *
     * @param key: the move key.
     */
    public void movePlayer(char key) {
        Position target = null;
        if (key == 'a') {
            target = new Position(player.x - 1, player.y);
        } else if (key == 'w') {
            target = new Position(player.x, player.y + 1);
        } else if (key == 's') {
            target = new Position(player.x, player.y - 1);
        } else if (key == 'd') {
            target = new Position(player.x + 1, player.y);
        }

        if (target != null && isFloor(target)) {
            swapTile(player, target);
            player = target;
        }
    }

    /**
     * Check whether the tile of the given position is floor.
     *
     * @param pos: the given position.
     * @return true if the tile is floor.
     */
    private boolean isFloor(Position pos) {
        int x = pos.x;
        int y = pos.y;
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT && board[x][y].equals(Tileset.FLOOR);
    }

    /**
     * Swap the tile of the given two position.
     *
     * @param p1: the first position.
     * @param p2: the second position.
     */
    private void swapTile(Position p1, Position p2) {
        TETile tile = board[p1.x][p1.y];
        board[p1.x][p1.y] = board[p2.x][p2.y];
        board[p2.x][p2.y] = tile;
    }

    /**
     * The random object we will use to generate the world.
     */
    private final Random random;

    /**
     * The grid.
     */
    TETile[][] board;

    /**
     * The position of player.
     */
    Position player;

    public static void main(String[] args) {
        TERenderer renderer;
        renderer = new TERenderer();
        Random rand = new Random();
        World world = new World(rand.nextInt());
        renderer.initialize(WIDTH, HEIGHT);
        renderer.renderFrame(world.board);
    }
}
