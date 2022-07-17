package hw4.puzzle;

import java.util.LinkedList;
import java.util.List;

public class Board implements WorldState {

    private static class Position {
        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Position(Position pos) {
            x = pos.x;
            y = pos.y;
        }

        int x;
        int y;
    }

    /**
     * Constructs a board from an N-by-N array of tiles where
     * tile[i][j] = tile at row i, column j.
     */
    public Board(int[][] tiles) {
        grids = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; ++i) {
            System.arraycopy(tiles[i], 0, grids[i], 0, tiles.length);
        }
        findEmpty();
    }

    /**
     * Find the empty position.
     */
    private void findEmpty() {
        for (int i = 0; i < size(); ++i) {
            for (int j = 0; j < size(); ++j) {
                if (tileAt(i, j) == 0) {
                    emptyPos = new Position(i, j);
                }
            }
        }
    }

    /**
     * Returns value of tile at (i, j), or 0 if blank.
     */
    public int tileAt(int i, int j) {
        return grids[i][j];
    }

    /**
     * Returns the board size N.
     */
    public int size() {
        return grids.length;
    }

    /**
     * Check bound.
     */
    private boolean inBound(Position pos) {
        return pos.x >= 0 && pos.x < size()
                && pos.y >= 0 && pos.y < size();
    }

    /**
     * Swap the value of two field.
     */
    private void swapValue(Position p1, Position p2) {
        int v1 = tileAt(p1.x, p1.y);
        int v2 = tileAt(p2.x, p2.y);
        grids[p1.x][p1.y] = v2;
        grids[p2.x][p2.y] = v1;
    }

    /**
     * Returns the neighbors of the current board.
     */
    public Iterable<WorldState> neighbors() {
        List<WorldState> neighbors = new LinkedList<>();

        for (int[] dir : dirs) {
            Position pos = new Position(emptyPos.x + dir[0], emptyPos.y + dir[1]);
            if (inBound(pos)) {
                swapValue(emptyPos, pos);
                Board neighbor = new Board(this.grids);
                neighbors.add(neighbor);
                swapValue(emptyPos, pos);
            }
        }

        return neighbors;
    }

    /**
     * Returns the correct value of (i, j) tile.
     */
    private int correctVal(int x, int y) {
        if (x == size() - 1 && y == size() - 1) {
            return 0;
        } else {
            return x * size() + y + 1;
        }
    }

    /**
     * The number of tiles in the wrong position (skip the last position).
     */
    public int hamming() {
        int dist = 0;
        for (int i = 0; i < size(); ++i) {
            for (int j = 0; j < size(); ++j) {
                // skip the last position.
                if (i == size() - 1 && j == size() - 1) {
                    continue;
                }
                if (tileAt(i, j) != correctVal(i, j)) {
                    dist += 1;
                }
            }
        }
        return dist;
    }

    /**
     * Return the correct position of a tile value (expect 0).
     */
    private Position correctPos(int value) {
        value -= 1;
        int x = value / size();
        int y = value % size();
        return new Position(x, y);
    }

    /**
     * Return the Manhattan distances of (x, y) tile.
     */
    private int tileManDist(int x, int y) {
        int value = tileAt(x, y);
        if (value == 0) {
            return 0;
        } else {
            Position pos = correctPos(value);
            return Math.abs(pos.x - x) + Math.abs(pos.y - y);
        }
    }

    /**
     * The sum of the Manhattan distances (sum of the vertical and
     * horizontal distance) from the tiles to their goal positions.
     */
    public int manhattan() {
        int dist = 0;
        for (int i = 0; i < size(); ++i) {
            for (int j = 0; j < size(); ++j) {
                dist += tileManDist(i, j);
            }
        }
        return dist;
    }

    /**
     * Estimated distance to goal.
     */
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    /**
     * Returns true if this board's tile values are the same
     * position as y's.
     */
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }

        if (this.getClass() != y.getClass()) {
            return false;
        }

        Board board = (Board) y;
        if (board.size() != size()) {
            return false;
        }

        for (int i = 0; i < size(); ++i) {
            for (int j = 0; j < size(); ++j) {
                if (tileAt(i, j) != board.tileAt(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns the string representation of the board.
     * Uncomment this method.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    private final int[][] grids;

    private final int[][] dirs = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};

    private Position emptyPos;
}
