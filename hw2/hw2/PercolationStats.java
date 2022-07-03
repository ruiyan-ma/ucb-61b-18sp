package hw2;

import edu.princeton.cs.introcs.StdStats;
import edu.princeton.cs.introcs.StdRandom;

public class PercolationStats {

    /**
     * Perform T independent experiments on an N-by-N grid.
     */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        this.N = N;
        this.T = T;
        factory = pf;
        data = new double[T];
        experiments();
    }

    /**
     * Do T experiments.
     */
    private void experiments() {
        for (int i = 0; i < T; i++) {
            data[i] = oneExp();
        }
    }

    /**
     * Do one experiment.
     */
    private double oneExp() {
        Percolation grid = factory.make(N);
        while (!grid.percolates()) {
            int row = StdRandom.uniform(N);
            int col = StdRandom.uniform(N);
            grid.open(row, col);
        }
        return (double) grid.numberOfOpenSites() / (N * N);
    }

    /**
     * Sample mean of percolation threshold.
     */
    public double mean() {
        return StdStats.mean(data);
    }

    /**
     * Sample standard deviation of percolation threshold.
     */
    public double stddev() {
        return StdStats.stddev(data);
    }

    /**
     * Low endpoint of 95% confidence interval.
     */
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    /**
     * High endpoint of 95% confidence interval.
     */
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

    /**
     * Size N.
     */
    private final int N;

    /**
     * Experiment time T.
     */
    private final int T;

    /**
     * Percolation factory.
     */
    private final PercolationFactory factory;

    /**
     * Data array.
     */
    private final double[] data;
}
