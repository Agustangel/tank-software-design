package ru.mipt.bit.platformer;

import java.util.List;

/**
 * Конкретная команда для движения танка
 */
public class MoveCommand implements Command {
    private final Tank tank;
    private final Direction direction;
    private final List<Collidable> collidables;

    public MoveCommand(Tank tank, Direction direction, List<Collidable> collidables) {
        this.tank = tank;
        this.direction = direction;
        this.collidables = collidables;
    }

    @Override
    public void execute() {
        tank.tryMove(direction, collidables);
    }

    public Tank getTank() {
        return tank;
    }

    public Direction getDirection() {
        return direction;
    }
}
