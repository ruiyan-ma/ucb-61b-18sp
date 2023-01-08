package byog.Core;


import java.util.Random;

public enum Direction {
    /**
     * Four directions.
     */
    left, right, up, down;

    /**
     * Convert an integer value into a direction.
     */
    public static Direction intToDirection(int value) {
        value %= 4;
        if (value == 0) {
            return left;
        } else if (value == 1) {
            return right;
        } else if (value == 2) {
            return up;
        } else {
            return down;
        }
    }
}
