package byog.Core;

import java.util.ArrayList;
import java.util.Random;

import static byog.Core.Game.HEIGHT;
import static byog.Core.Game.WIDTH;

public class Hallway extends Room {

    /**
     * Generate a random sized hallway.
     *
     * @param random:    used to generate random length.
     * @param connector: the start connector.
     */
    Hallway(Random random, Connector connector) {
        super(connector.pos);
        Position pos = connector.pos;
        Direction dir = connector.dir;

        int size = RandomUtils.uniform(random, MIN_LENGTH, MAX_LENGTH + 1);
        if (dir == Direction.left || dir == Direction.right) {
            if (dir == Direction.left) {
                right = inBoundVal(pos.x, WIDTH);
                left = inBoundVal(right - size, WIDTH);
            } else {
                left = inBoundVal(pos.x, WIDTH);
                right = inBoundVal(left + size, WIDTH);
            }
        } else {
            if (dir == Direction.up) {
                bottom = inBoundVal(pos.y, HEIGHT);
                top = inBoundVal(bottom + size, HEIGHT);
            } else {
                top = inBoundVal(pos.y, HEIGHT);
                bottom = inBoundVal(top - size, HEIGHT);
            }
        }

        setConnectors(random);
    }

    @Override
    protected void setConnectors(Random random) {
        connectors = new ArrayList<>();
        if (top == bottom) {
            Position leftPoint = new Position(left - 1, top);
            Position rightPoint = new Position(right + 1, top);
            connectors.add(new Connector(leftPoint, Direction.left, ROOM_TYPE));
            connectors.add(new Connector(rightPoint, Direction.right, ROOM_TYPE));
        } else {
            Position topPoint = new Position(left, top + 1);
            Position bottomPoint = new Position(left, bottom - 1);
            connectors.add(new Connector(topPoint, Direction.up, ROOM_TYPE));
            connectors.add(new Connector(bottomPoint, Direction.down, ROOM_TYPE));
        }
    }
}
