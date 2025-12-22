package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.api.Direction;
import ru.mipt.bit.platformer.api.GameWorld;
import ru.mipt.bit.platformer.controller.GameObjectController;

/**
 * Команда для движения вверх
 * SRP: отвечает ТОЛЬКО за создание движения вверх
 */
public class MoveUpCommand implements Command {
    private final GameObjectController controller;
    
    public MoveUpCommand(GameObjectController controller) {
        this.controller = controller;
    }
    
    @Override
    public void execute(GameWorld gameWorld) {
        controller.move(gameWorld, Direction.UP);
    }
}
