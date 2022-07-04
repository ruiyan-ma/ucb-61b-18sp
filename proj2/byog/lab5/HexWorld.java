package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final int WIDTH = 50;

    private static final int HEIGHT = 50;

    private static final Random RANDOM = new Random();

    /**
     * Nested position class to represent a coordinate.
     */
    public static class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Computes the width of row index for a given size hexagon.
     *  xx     index = 3, effectIndex = 0
     * xxxx    index = 2, effectIndex = 1
     * xxxx    index = 1, effectIndex = 1
     *  xx     index = 0, effectIndex = 0
     *
     * @param size  The size of the hexagon
     * @param index The row number where index = 0 is the bottom row
     */
    private static int hexRowWidth(int size, int index) {
        int effectIndex = index;
        if (index >= size) {
            effectIndex = 2 * size - 1 - index;
        }

        return size + 2 * effectIndex;
    }

    /**
     * Compute the x coordinate of the leftmost tile in the ith
     * row of a hexagon, assuming that the bottom row has an x-coordinate
     * of zero. For example, if size = 3, and index = 2, this function
     * returns -2, because the row 2 up from the bottom starts 2 to the left
     * of the start position, e.g.
     *   xxx
     *  xxxxx
     * xxxxxxx
     * xxxxxxx <-- index = 2, starts 2 spots to the left of the bottom of the hex
     *  xxxxx
     *   xxx
     *
     * @param size  size of the hexagon
     * @param index row num of the hexagon, where i = 0 is the bottom
     */
    private static int hexRowOffset(int size, int index) {
        int effectIndex = index;
        if (index >= size) {
            effectIndex = 2 * size - 1 - effectIndex;
        }
        return -effectIndex;
    }

    /**
     * Adds a row of the same tile.
     */
    private static void addRow(TETile[][] world, Position pos, int width, TETile tile) {
        for (int x = 0; x < width; ++x) {
            world[pos.x + x][pos.y] = tile;
        }
    }

    /**
     * Add a hexagon on a given position.
     * Position pos specifies the lower corner of the hexagon.
     */
    private static void addHexagon(TETile[][] world, Position pos, int size, TETile tile) {
        for (int rowIndex = 0; rowIndex < 2 * size; ++rowIndex) {
            int yStartPos = pos.y + rowIndex;
            int xStartPos = pos.x + hexRowOffset(size, rowIndex);

            Position startPos = new Position(xStartPos, yStartPos);
            int rowWidth = hexRowWidth(size, rowIndex);

            addRow(world, startPos, rowWidth, tile);
        }
    }

    /**
     * Return a random tile.
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(6);
        return switch (tileNum) {
            case 0 -> Tileset.GRASS;
            case 1 -> Tileset.WATER;
            case 2 -> Tileset.FLOWER;
            case 3 -> Tileset.SAND;
            case 4 -> Tileset.MOUNTAIN;
            case 5 -> Tileset.TREE;
            default -> Tileset.NOTHING;
        };
    }

    /**
     * Draws a column of num hexagons, each one with a random tile.
     *
     * @param world The world
     * @param pos The position of the bottom left corner of the bottom hexagon
     * @param size The size of hexagons
     * @param num The number of hexagons to draw
     */
    private static void drawRandomVerticalHexes(TETile[][] world, Position pos, int size, int num) {
        for (int i = 0; i < num; ++i) {
            addHexagon(world, pos, size, randomTile());
            pos = new Position(pos.x, pos.y + 2 * size);
        }
    }

    /**
     * Return the position of bottom right hexagon.
     */
    private static Position bottomRightPos(Position pos, int size) {
        int xNewPos = pos.x + 2 * size - 1;
        int yNewPos = pos.y - size;
        return new Position(xNewPos, yNewPos);
    }

    /**
     * Return the position of top right hexagon.
     */
    private static Position topRightPos(Position pos, int size) {
        int xNewPos = pos.x + 2 * size - 1;
        int yNewPos = pos.y + size;
        return new Position(xNewPos, yNewPos);
    }

    /**
     * Draw 19 hexagons.
     */
    private static void draw19Hexagons(TETile[][] world, Position pos, int size) {
        drawRandomVerticalHexes(world, pos, size, 3);

        pos = bottomRightPos(pos, size);
        drawRandomVerticalHexes(world, pos, size, 4);

        pos = bottomRightPos(pos, size);
        drawRandomVerticalHexes(world, pos, size, 5);

        pos = topRightPos(pos, size);
        drawRandomVerticalHexes(world, pos, size, 4);

        pos = topRightPos(pos, size);
        drawRandomVerticalHexes(world, pos, size, 3);
    }

    /**
     * Fill the world with nothing.
     */
    private static void fillWithNothing(TETile[][] world) {
        for (int i = 0; i < WIDTH; ++i) {
            for (int j = 0; j < HEIGHT; ++j) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    public static void main(String[] args) {
        TERenderer renderer = new TERenderer();
        renderer.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        fillWithNothing(world);

        Position pos = new Position(15, 15);
        draw19Hexagons(world, pos, 3);
        renderer.renderFrame(world);
    }
}
