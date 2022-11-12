package byog.Core;


import java.util.Random;

public enum Direction {
    /**
     * Four directions.
     */
    left, right, up, down;

    /**
     * Return the reverse direction.
     */
    public static Direction reverse(Direction dir) {
        if (dir == left) {
            return right;
        } else if (dir == right) {
            return left;
        } else if (dir == up) {
            return down;
        } else {
            return up;
        }
    }

    /**
     * Generate a random direction.
     */
    public static Direction randomDir(Random random) {
        int randInt = random.nextInt(4);

        if (randInt == 0) {
            return left;
        } else if (randInt == 1) {
            return right;
        } else if (randInt == 2) {
            return up;
        } else {
            return down;
        }
    }
}
