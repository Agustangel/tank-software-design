package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.api.Direction;
import ru.mipt.bit.platformer.api.GameWorld;
import ru.mipt.bit.platformer.controller.GameObjectController;

/**
 * Команда для движения вправо
 */
public class MoveRightCommand implements Command {
    private final GameObjectController controller;
    
    public MoveRightCommand(GameObjectController controller) {
        this.controller = controller;
    }
    
    @Override
    public void execute(GameWorld gameWorld) {
        controller.move(gameWorld, Direction.RIGHT);
    }
}
