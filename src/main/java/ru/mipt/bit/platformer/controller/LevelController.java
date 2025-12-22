package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.api.GameWorld;
import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.graphics.LevelGraphics;

import java.util.*;

/**
 * Главный Controller уровня
 * SRP: управляет игровым циклом и координирует всех Controllers
 */
public class LevelController {
    private final Level levelModel;
    private final LevelGraphics levelGraphics;
    private final GameWorld gameWorld;
    
    private TankController tankController;
    private final Set<GameObjectController> botControllers = new HashSet<>();
    private final Set<BulletController> bulletControllers = new HashSet<>();
    private final Set<ObstacleController> obstacleControllers = new HashSet<>();
    
    public LevelController(Level levelModel, LevelGraphics levelGraphics) {
        this.levelModel = levelModel;
        this.levelGraphics = levelGraphics;
        this.gameWorld = levelModel;
    }
    
    public void setTankController(TankController controller) {
        this.tankController = controller;
    }
    
    public void addBotController(GameObjectController controller) {
        botControllers.add(controller);
    }
    
    public void addObstacleController(ObstacleController controller) {
        obstacleControllers.add(controller);
    }
    
    public void addBulletController(BulletController controller) {
        bulletControllers.add(controller);
    }
    
    /**
     * Обработать команды от InputHandler
     */
    public void handleCommands(Queue<Command> commands) {
        while (!commands.isEmpty()) {
            Command command = commands.poll();
            command.execute(gameWorld);
        }
    }
    
    /**
     * Обновить всех Controllers
     */
    public void update(float delta) {
        // Обновить игрока
        if (tankController != null) {
            tankController.update(delta);
        }
        
        // Обновить ботов
        botControllers.forEach(controller -> controller.update(delta));
        
        // Обновить пули
        bulletControllers.forEach(controller -> controller.update(delta));
        
        // Удалить уничтоженные пули
        bulletControllers.removeIf(controller -> 
            controller.getModel().isDestroyed());
        
        // Обновить препятствия
        obstacleControllers.forEach(controller -> controller.update(delta));
    }
    
    /**
     * Отрисовать всё
     */
    public void render(Batch batch) {
        levelGraphics.render(batch);
        
        if (tankController != null) {
            tankController.render();
            batch.begin();
            tankController.getGraphics().render(batch);
            batch.end();
        }
        
        botControllers.forEach(controller -> {
            controller.render();
            batch.begin();
            controller.getGraphics().render(batch);
            batch.end();
        });
        
        bulletControllers.forEach(controller -> {
            controller.render();
            batch.begin();
            controller.getGraphics().render(batch);
            batch.end();
        });
        
        obstacleControllers.forEach(controller -> {
            controller.render();
            batch.begin();
            controller.getGraphics().render(batch);
            batch.end();
        });
    }
    
    /**
     * Очистить ресурсы
     */
    public void dispose() {
        levelGraphics = null;
    }
    
    public TankController getTankController() {
        return tankController;
    }
    
    public Set<GameObjectController> getBotControllers() {
        return botControllers;
    }
}
