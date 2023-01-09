package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class Game {
    TERenderer ter = new TERenderer();

    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        setCanvas();

        char input = solicitInputInit();
        World world = null;
        if (input == 'n') {
            long seed = inputSeed();
            world = new World(seed);
        } else if (input == 'l') {
            world = loadGame();
        } else if (input == 'q') {
            System.exit(0);
        }

        playGame(world);
    }

    /**
     * Set canvas and show texts for playWithKeyboard.
     */
    private void setCanvas() {
        StdDraw.setCanvasSize(640, 640);
        StdDraw.setXscale(0, 640);
        StdDraw.setYscale(0, 640);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 30));
        StdDraw.text(320, 480, "CS61B: THE GAME");

        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 20));
        StdDraw.text(320, 345, "New Game (N)");
        StdDraw.text(320, 320, "Load Game (L)");
        StdDraw.text(320, 295, "Quit (Q)");

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /**
     * Get initial input from user: N, L or Q.
     *
     * @return the user input
     */
    private char solicitInputInit() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (key == 'n' || key == 'l' || key == 'q') {
                    return key;
                }
            }
        }
    }

    /**
     * Get a random seed from user input.
     */
    private long inputSeed() {
        StdDraw.clear(Color.BLACK);
        StdDraw.text(320, 320, "Enter seed. Press S to end.");
        StdDraw.show();

        StringBuilder builder = new StringBuilder();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());

                if (key == 's') {
                    break;
                }

                builder.append(key);
                StdDraw.clear(Color.BLACK);
                StdDraw.text(320, 320, "Enter seed. Press S to end.");
                StdDraw.text(320, 300, "Your input: " + builder);
                StdDraw.show();
            }
        }

        return Long.parseLong(builder.toString());
    }

    private void playGame(World world) {
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world.board);
//        showTileInfo(world);

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (key == ':') {
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char exit = Character.toLowerCase(StdDraw.nextKeyTyped());
                            if (exit == 'q') {
//                                saveWorld(board);
                                System.exit(0);
                            } else {
                                break;
                            }
                        }
                    }
                }

                world.movePlayer(key);
                ter.renderFrame(world.board);
//                showTileInfo(world);
            }
        }
    }

    public void showTileInfo(World world) {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        String text = "";
        if (x < WIDTH && y < HEIGHT) {
            text = world.board[x][y].description();
            StdDraw.setPenColor(Color.white);
            Font smallFont = new Font("Monaco", Font.PLAIN, 20);
            StdDraw.setFont(smallFont);
            StdDraw.textLeft(1, 29, text);
        }
        StdDraw.show();
        ter.renderFrame(world.board);
    }

    /**
     * Method used for auto-grading and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        input = input.toLowerCase();
        World board = setUpWorld(input);
        moveByInput(board, input);
        saveGame(board, input);
        return board.board;
    }

    /**
     * Set up the world.
     *
     * @param input: the user input.
     * @return the world.
     */
    private World setUpWorld(String input) {
        World board;
        if (input.charAt(0) == 'n') {
            int endIndex = input.indexOf("s");
            long seed = Long.parseLong(input.substring(1, endIndex));
            board = new World(seed);
        } else if (input.charAt(0) == 'l') {
            board = loadGame();
        } else {
            throw new IllegalArgumentException("Input must start with N or L!");
        }

        return board;
    }

    /**
     * Move the player according to the input.
     *
     * @param world: the world.
     * @param input: the user input.
     */
    private void moveByInput(World world, String input) {
        int start = Math.max(input.indexOf('s'), input.indexOf('l')) + 1;
        int end = input.indexOf(":q");
        if (end == -1) {
            end = input.length();
        }

        for (int i = start; i < end; ++i) {
            world.movePlayer(input.charAt(i));
        }
    }

    /**
     * Load the world.
     *
     * @return the loaded world.
     */
    private World loadGame() {
        return null;
    }

    /**
     * Save the game if we need.
     * @param world: the world.
     * @param input: the user input.
     */
    private void saveGame(World world, String input) {
        if (input.indexOf(":q") > 0) {
            // save game
        }
    }


}
