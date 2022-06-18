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

    /**
     * Add a hexagon of a given side length to a given position,
     * start from the left conner.
     */
    public static void addHexagon(TETile[][] tiles, int x, int y, int length) {
        int width = length + 2 * (length - 1);
        int tileNum = length;

        for (int i = 0; i < length; ++i) {
            fillRow(tiles, x, y + i, tileNum, width);
            fillRow(tiles, x, y + 2 * length - 1 - i, tileNum, width);
            tileNum += 2;
        }
    }

    /**
     * Fill a single line with tiles.
     */
    private static void fillRow(TETile[][] tiles, int x, int y, int tileNum, int colNum) {
        int space = (colNum - tileNum) / 2;
        for (int i = space; i < colNum - space; i++) {
            tiles[x + i][y] = Tileset.FLOWER;
        }
    }

    /**
     * Fill the entire map with nothing.
     */
    private static void fillNothing(TETile[][] tiles) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tiles[i][j] = Tileset.NOTHING;
            }
        }
    }

    public static void main(String[] args) {
        TERenderer render = new TERenderer();
        render.initialize(WIDTH, HEIGHT);

        TETile[][] hexagonTiles = new TETile[WIDTH][HEIGHT];
        fillNothing(hexagonTiles);

        addHexagon(hexagonTiles, 0, 0, 4);
        addHexagon(hexagonTiles, 20, 20, 5);

        render.renderFrame(hexagonTiles);
    }
}
