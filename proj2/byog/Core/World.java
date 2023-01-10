package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.io.Serializable;
import java.util.*;

import static byog.Core.Game.HEIGHT;
import static byog.Core.Game.WIDTH;
import static byog.Core.Room.ROOM_TYPE;

public class World implements Serializable {

    public static final int MAX_RANDOM_TRY = 5;

    World() {
        random = new Random();
        board = new TETile[WIDTH][HEIGHT];

        fillWithNothing(board);
        createWorld();
        generatePlayers();
    }

    World(long seed) {
        random = new Random(seed);
        board = new TETile[WIDTH][HEIGHT];

        fillWithNothing(board);
        createWorld();
        generatePlayers();
    }

    /**
     * Fill the board with nothing.
     */
    private void fillWithNothing(TETile[][] board) {
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
    private void generatePlayers() {
        players = new ArrayList<>();

        Position pos1 = randomPosOnFloor();
        Player player1 = new Player(pos1, 'a', 'd', 'w', 's');

        Position pos2 = randomPosOnFloor();
        while (pos2.equals(pos1)) {
            pos2 = randomPosOnFloor();
        }
        Player player2 = new Player(pos2, 'h', 'l', 'k', 'j');

        players.add(player1);
        board[player1.pos.x][player1.pos.y] = Tileset.PLAYER;

        players.add(player2);
        board[player2.pos.x][player2.pos.y] = Tileset.PLAYER;
    }

    /**
     * Generate a random position on floor.
     *
     * @return the position.
     */
    private Position randomPosOnFloor() {
        Position pos = new Position(random.nextInt(WIDTH), random.nextInt(HEIGHT));
        while (!isFloor(pos)) {
            pos.x = random.nextInt(WIDTH);
            pos.y = random.nextInt(HEIGHT);
        }
        return pos;
    }

    /**
     * Move the player according to the given key.
     *
     * @param key: the move key.
     */
    public void movePlayer(char key) {
        for (Player player : players) {
            Position target = null;
            if (key == player.left) {
                target = new Position(player.pos.x - 1, player.pos.y);
            } else if (key == player.right) {
                target = new Position(player.pos.x + 1, player.pos.y);
            } else if (key == player.up) {
                target = new Position(player.pos.x, player.pos.y + 1);
            } else if (key == player.down) {
                target = new Position(player.pos.x, player.pos.y - 1);
            }

            if (target != null && isFloor(target)) {
                swapTile(player.pos, target);
                player.pos = target;
            }
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

    public TETile[][] getBoard() {
        return board;
    }

    /**
     * The random object we will use to generate the world.
     */
    private final Random random;

    /**
     * The grid.
     */
    private final TETile[][] board;

    /**
     * The position of player.
     */
    private List<Player> players;

    public static void main(String[] args) {
        TERenderer renderer;
        renderer = new TERenderer();
        Random rand = new Random();
        World world = new World(rand.nextInt());
        renderer.initialize(WIDTH, HEIGHT);
        renderer.renderFrame(world.board);
    }
}
