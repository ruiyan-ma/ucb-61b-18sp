package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {

    public static final int WIDTH = 50;

    public static final int HEIGHT = 50;

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

    /**
     * Create rooms, and return the room list.
     */
    private List<Room> createRooms() {
        int roomNum = RandomUtils.uniform(random, 15, 30);
        List<Room> newRooms = new ArrayList<>(roomNum);

        while (newRooms.size() < roomNum) {
            Room room = new Room(random, WIDTH, HEIGHT);
            if (room.noOverlap(newRooms)) {
                room.drawRoom(grid);
                newRooms.add(room);
            }
        }

        return newRooms;
    }

    /**
     * Create hallways for a room, and return the hallways list.
     */
    private List<Room> createHallways(Room room, int limit) {
        int hallwayNum = RandomUtils.uniform(random, 1, limit);
        List<Room> hallways = new ArrayList<>(hallwayNum);

        for (int i = 0; i < hallwayNum; ++i) {
            Connector connector = room.connector(random);
            Hallway hallway = new Hallway(random, connector, WIDTH, HEIGHT);
            hallway.drawRoom(grid);
            hallways.add(hallway);
        }

        return hallways;
    }

    private TETile getTile(Position p) {
        return grid[p.x][p.y];
    }

    private void setTile(Position p, TETile tile) {
        grid[p.x][p.y] = tile;
    }

    private boolean isTile(Position p, TETile tile) {
        return getTile(p).equals(tile);
    }

    private boolean isNothing(Position p) {
        return isTile(p, Tileset.NOTHING);
    }

    private boolean isFloor(Position p) {
        return isTile(p, Tileset.FLOOR);
    }

    private boolean isWall(Position p) {
        return isTile(p, Tileset.WALL);
    }

    private void checkEnter(Position curr, Position next) {
        if (isNothing(curr) && isFloor(next)) {
            setTile(curr, Tileset.WALL);
        }
    }

    private void checkLeave(Position curr, Position next) {
        if (isFloor(curr) && isNothing(next)) {
            setTile(next, Tileset.WALL);
        }
    }

    private boolean inBound(Position p) {
        int x = p.x, y = p.y;
        return x >= 0 && x < WIDTH
                && y >= 0 && y < HEIGHT;
    }

    private void fillWall(Position p) {
        int x = p.x, y = p.y;
        Position left = new Position(x - 1, y),
                right = new Position(x + 1, y),
                up = new Position(x, y + 1),
                down = new Position(x, y - 1);

        if (inBound(left) && isNothing(left)
                && inBound(right) && isNothing(right)) {
            setTile(left, Tileset.WALL);
            setTile(right, Tileset.WALL);
        }

        if (inBound(up) && isNothing(up)
                && inBound(down) && isNothing(down)) {
            setTile(up, Tileset.WALL);
            setTile(down, Tileset.WALL);
        }
    }

    /**
     * Create wall.
     */
    private void createWall() {
        // Scan from left to right
        for (int j = 0; j < HEIGHT; ++j) {
            for (int i = 0; i < WIDTH - 1; ++i) {
                Position curr = new Position(i, j);
                Position next = new Position(i + 1, j);

                checkEnter(curr, next);
                checkLeave(curr, next);
            }
        }

        // Scan from bottom to tp
        for (int i = 0; i < WIDTH; ++i) {
            for (int j = 0; j < HEIGHT - 1; ++j) {
                Position curr = new Position(i, j);
                Position next = new Position(i, j + 1);

                checkEnter(curr, next);
                checkLeave(curr, next);
            }
        }

        for (int i = 0; i < WIDTH; ++i) {
            for (int j = 0; j < HEIGHT; ++j) {
                Position curr = new Position(i, j);
                if (isWall(curr)) {
                    fillWall(curr);
                }
            }
        }
    }

    /**
     * Create the world.
     */
    public void createWorld() {
        List<Room> rooms = createRooms();
        for (Room room : rooms) {
            List<Room> hallways = createHallways(room, 4);
            Room hallway = hallways.get(random.nextInt(hallways.size()));
            createHallways(hallway, 2);
        }

        createWall();
    }

    Random random;

    /**
     * The grid.
     */
    TETile[][] grid;


    public static void main(String[] args) {
        TERenderer renderer = new TERenderer();
        Board board = new Board(new Random());
        renderer.initialize(WIDTH, HEIGHT);

        board.fillWithNothing();

        board.createWorld();
        renderer.renderFrame(board.grid);
    }
}
