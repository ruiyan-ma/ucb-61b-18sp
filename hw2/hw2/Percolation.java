package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    /**
     * Create N-by-N gird, with all sites initially blocked.
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be greater than 0!");
        }
        this.N = N;
        openCount = 0;
        opened = new boolean[N][N];
        unionFind = new WeightedQuickUnionUF(N * N + 2);
    }

    /**
     * Check the row and column indices bounds.
     */
    private void checkBounds(int row, int col) {
        if (row < 0 || row >= N) {
            throw new IndexOutOfBoundsException("Row index " + row + " out of bound!");
        }

        if (col < 0 || col >= N) {
            throw new IndexOutOfBoundsException("Column index " + col + " out of bound!");
        }
    }

    /**
     * Convert (row, col) coordinate into 1D index.
     * <p>
     * The first cell is the virtual top site, thus we need +1.
     */
    private int convertTo1D(int row, int col) {
        return row * N + col + 1;
    }

    /**
     * Open the site (row, col) if it is not open already.
     */
    public void open(int row, int col) {
        checkBounds(row, col);
        if (!opened[row][col]) {
            opened[row][col] = true;
            openCount += 1;

            // Connect the current site to its surrounding open sites.
            int index = convertTo1D(row, col);
            for (int[] dir : dirs) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                if (newRow >= 0 && newRow < N
                        && newCol >= 0 && newCol < N
                        && isOpen(newRow, newCol)) {
                    int newIndex = convertTo1D(newRow, newCol);
                    unionFind.union(index, newIndex);
                }
            }

            // if row == 0, connect it with the virtual top site.
            if (row == 0) {
                unionFind.union(index, 0);
            }

            // if row == N - 1, connect it  with the virtual bottom site.
            if (row == N - 1) {
                unionFind.union(index, N * N + 1);
            }
        }
    }

    /**
     * Is the site (row, col) open?
     */
    public boolean isOpen(int row, int col) {
        checkBounds(row, col);
        return opened[row][col];
    }

    /**
     * Is the site (row, col) full?
     */
    public boolean isFull(int row, int col) {
        checkBounds(row, col);
        int index = convertTo1D(row, col);
        return unionFind.connected(index, 0);
    }

    /**
     * Number of open sites.
     */
    public int numberOfOpenSites() {
        return openCount;
    }

    /**
     * Does the system percolate?
     */
    public boolean percolates() {
        return unionFind.connected(0, N * N + 1);
    }

    /**
     * Size N.
     */
    private final int N;

    /**
     * The number of opened sites.
     */
    private int openCount;

    /**
     * (N * N) 2D grid to record opened information.
     */
    private final boolean[][] opened;

    /**
     * (N * N + 2) 1D union find structure to record connected information.
     * <p>
     * The first cell is the virtual top site, the last cell is the virtual bottom site.
     */
    private final WeightedQuickUnionUF unionFind;

    /**
     * 4 directions of a site.
     */
    private final int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
}
