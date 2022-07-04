package byog.Core;

import java.util.Random;

public class Connector {

    Connector(Random random, Room room) {
        int randInt = random.nextInt(4);

        if (randInt == 0) {             // left
            int x = room.left;
            int y = RandomUtils.uniform(random, room.bottom, room.top + 1);
            pos = new Position(x, y);
            dir = Direction.left;
        } else if (randInt == 1) {      // right
            int x = room.right;
            int y = RandomUtils.uniform(random, room.bottom, room.top + 1);
            pos = new Position(x, y);
            dir = Direction.right;
        } else if (randInt == 2) {      // up
            int y = room.top;
            int x = RandomUtils.uniform(random, room.left, room.right + 1);
            pos = new Position(x, y);
            dir = Direction.up;
        } else {                        // down
            int y = room.bottom;
            int x = RandomUtils.uniform(random, room.left, room.right + 1);
            pos = new Position(x, y);
            dir = Direction.down;
        }
    }

    Position pos;

    Direction dir;
}
