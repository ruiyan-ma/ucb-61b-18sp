import java.util.*;

import edu.princeton.cs.algs4.In;

public class Boggle {

    /**
     * Solves a Boggle puzzle.
     *
     * @param k             The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     * The Strings are sorted in descending order of length.
     * If multiple words have the same length,
     * have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        if (k <= 0) {
            throw new IllegalArgumentException("K should be greater than zero.");
        }

        limit = k;
        board = buildBoard(boardFilePath);
        trie = buildTrie();
        wordSet = new TreeSet<>((o1, o2) -> {
            if (o1.length() != o2.length()) {
                return o2.length() - o1.length();
            } else {
                return o1.compareTo(o2);
            }
        });

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                backtrack(new StringBuilder(), new boolean[m][n], i, j);
            }
        }

        return new ArrayList<>(wordSet);
    }

    private static char[][] buildBoard(String boardFilePath) {
        String[] strings = new In(boardFilePath).readAllLines();
        m = strings.length;
        n = strings[0].length();

        char[][] b = new char[m][n];
        for (int i = 0; i < m; ++i) {
            b[i] = strings[i].toCharArray();
        }

        return b;
    }

    private static Trie buildTrie() {
        String[] words = new In(dictPath).readAllLines();
        Trie t = new Trie();
        for (String word : words) {
            t.add(word);
        }
        return t;
    }

    private static void backtrack(StringBuilder builder, boolean[][] visited, int r, int c) {
        if (!inBound(r, c) || visited[r][c] || !trie.findPrefix(builder.toString())) {
            return;
        }

        builder.append(board[r][c]);
        visited[r][c] = true;

        String str = builder.toString();
        if (str.length() >= LENGTH && trie.findWord(str)) {
            wordSet.add(str);
            if (wordSet.size() > limit) {
                wordSet.pollLast();
            }
        }

        for (int[] dir : dirs) {
            int row = r + dir[0];
            int col = c + dir[1];
            backtrack(builder, visited, row, col);
        }

        // backtrack
        builder.deleteCharAt(builder.length() - 1);
        visited[r][c] = false;
    }

    private static boolean inBound(int r, int c) {
        return r >= 0 && r < m && c >= 0 && c < n;
    }

    private static char[][] board;

    private static int m, n, limit;

    private static final int LENGTH = 3;

    private static int[][] dirs = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};

    private static Trie trie;

    private static TreeSet<String> wordSet;

    static String dictPath = "words.txt";
}
