package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.Command;
import ru.mipt.bit.platformer.Tank;

/**
 * Конкретная команда для стрельбы танка
 */
public class ShootCommand implements Command {
    private final Tank tank;

    public ShootCommand(Tank tank) {
        this.tank = tank;
    }

    @Override
    public void execute() {
        tank.shoot();
    }

    public Tank getTank() {
        return tank;
    }
}
