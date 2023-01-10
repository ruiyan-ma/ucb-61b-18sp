package byog.Core;

import java.io.Serializable;

public class Player implements Serializable {

    Player(Position pos, char left, char right, char up, char down) {
        this.pos = pos;
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
    }

    Position pos;

    char left;

    char right;

    char up;

    char down;
}
