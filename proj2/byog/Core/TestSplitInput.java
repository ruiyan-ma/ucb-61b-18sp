package byog.Core;

import byog.TileEngine.TETile;

import java.util.ArrayList;
import java.util.List;

public class TestSplitInput {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add(test1());
        list.add(test2());
        list.add(test3());
        list.add(test4());

        for (int i = 0; i < list.size() - 1; ++i) {
            if (!list.get(i).equals(list.get(i + 1))) {
                System.out.println("not equal");
                System.out.println(list.get(i));
                System.out.println(list.get(i + 1));
            }
        }
    }

    private static String test1() {
        Game game = new Game();
        TETile[][] board = game.playWithInputString("N999SDDDWWWDDD");
        return TETile.toString(board);
    }

    private static String test2() {
        Game game = new Game();
        game.playWithInputString("N999SDDD:Q");
        TETile[][] board = game.playWithInputString("LWWWDDD");
        return TETile.toString(board);
    }

    private static String test3() {
        Game game = new Game();
        game.playWithInputString("N999SDDD:Q");
        game.playWithInputString("LWWW:Q");
        TETile[][] board = game.playWithInputString("LDDD:Q");
        return TETile.toString(board);
    }

    private static String test4() {
        Game game = new Game();
        game.playWithInputString("N999SDDD:Q");
        game.playWithInputString("L:Q");
        game.playWithInputString("L:Q");
        TETile[][] board = game.playWithInputString("LWWWDDD");
        return TETile.toString(board);
    }
}
