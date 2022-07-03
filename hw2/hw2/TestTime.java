package hw2;

import edu.princeton.cs.algs4.Stopwatch;

public class TestTime {
    public static void main(String[] args) {
        for (int N = 10; N < 500; N *= 2) {
            test(N);
        }
    }

    private static void test(int N) {
        Stopwatch watch = new Stopwatch();
        PercolationStats stats = new PercolationStats(N, 30, new PercolationFactory());
        System.out.println("N = " + N + ", time = " + watch.elapsedTime());
    }
}
