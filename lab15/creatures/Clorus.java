package creatures;

import huglife.*;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class Clorus extends Creature {
    public Clorus(double e) {
        super("clorus");
        energy = e;
    }

    public Clorus() {
        this(1);
    }

    @Override
    public void move() {
        energy -= 0.03;
    }

    @Override
    public void attack(Creature c) {
        energy += c.energy();
    }

    @Override
    public Creature replicate() {
        energy *= 0.5;
        return new Clorus(energy);
    }

    @Override
    public void stay() {
        energy -= 0.01;
    }

    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> plips = getNeighborsOfType(neighbors, "plip");

        if (empties.size() == 0) {
            return new Action(Action.ActionType.STAY);
        }

        if (plips.size() > 0) {
            Direction dir = HugLifeUtils.randomEntry(plips);
            return new Action(Action.ActionType.ATTACK, dir);
        }

        if (energy >= 1) {
            Direction dir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.REPLICATE, dir);
        }

        Direction dir = HugLifeUtils.randomEntry(empties);
        return new Action(Action.ActionType.MOVE, dir);
    }

    @Override
    public Color color() {
        return new Color(34, 0, 231);
    }

    private double energy;
}
