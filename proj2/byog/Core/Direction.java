package byog.Core;


public enum Direction {
    /**
     * Four directions.
     */
    left, right, up, down;

    /**
     * Reverse the given direction.
     *
     * @param direction: the given direction.
     * @return: the reversed direction.
     */
    public static Direction reverse(Direction direction) {
        if (direction == left) {
            return right;
        } else if (direction == right) {
            return left;
        } else if (direction == up) {
            return down;
        } else if (direction == down) {
            return up;
        }

        return null;
    }
}
