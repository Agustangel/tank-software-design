package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.api.GameWorld;
import ru.mipt.bit.platformer.controller.GameObjectController;

/**
 * Команда для включения/выключения отображения здоровья
 */
public class ToggleHealthBarCommand implements Command {
    private final GameObjectController controller;
    
    public ToggleHealthBarCommand(GameObjectController controller) {
        this.controller = controller;
    }
    
    @Override
    public void execute(GameWorld gameWorld) {
        controller.toggleHealthBar();
    }
}
