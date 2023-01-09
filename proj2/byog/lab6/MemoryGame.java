package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Random;

public class MemoryGame {
    private final int width;
    private final int height;
    private int round;
    private final Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
            "You got this!", "You're a star!", "Go Bears!",
            "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(seed, 40, 40);
        game.startGame();
    }

    public MemoryGame(int seed, int width, int height) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        StringBuilder builder = new StringBuilder();
        while (builder.length() < n) {
            builder.append(CHARACTERS[rand.nextInt(26)]);
        }
        return builder.toString();
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        double midWidth = (double) width / 2;
        double midHeight = (double) height / 2;

        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(midWidth, midHeight, s);

        //TODO: If game is not over, display relevant game information at the top of the screen
        if (!gameOver) {
            StdDraw.setFont(new Font("Monaco", Font.BOLD, 15));
            StdDraw.textLeft(0, height - 1, "Round: " + round);
            StdDraw.textRight(width - 1, height - 1, ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)]);

            if (playerTurn) {
                StdDraw.text(midWidth, height - 1, "Type!");
            } else {
                StdDraw.text(midWidth, height - 1, "Watch!");
            }

            StdDraw.line(0, height - 2, width, height - 2);
        }

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        for (int i = 0; i < letters.length(); ++i) {
            drawFrame(letters.substring(i, i + 1));
            StdDraw.pause(1000);
            drawFrame("");
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        StringBuilder builder = new StringBuilder();
        while (builder.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                builder.append(StdDraw.nextKeyTyped());
                drawFrame(builder.toString());
            }
        }
        StdDraw.pause(500);
        return builder.toString();
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        gameOver = false;
        playerTurn = false;
        round = 1;

        //TODO: Establish Game loop
        while (!gameOver) {
            playerTurn = false;
            drawFrame("Round: " + round);
            StdDraw.pause(1000);

            String target = generateRandomString(round);
            flashSequence(target);

            playerTurn = true;
            String input = solicitNCharsInput(round);

            if (input.equals(target)) {
                drawFrame("Correct! Well done!");
                StdDraw.pause(1000);
                round += 1;
            } else {
                gameOver = true;
                drawFrame("Game Over! You made it to round: " + round);
            }
        }
    }
}
