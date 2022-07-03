package hw2;

import org.junit.Test;
import static org.junit.Assert.*;

public class PercolationTest {

    @Test
    public void test3Grid() {
        Percolation grid = new Percolation(3);
        grid.open(0, 0);
        grid.open(1, 1);
        grid.open(2, 2);

        // not percolate
        assertFalse(grid.percolates());

        grid.open(1, 0);
        grid.open(2, 1);

        // percolate
        assertTrue(grid.percolates());
    }
}
