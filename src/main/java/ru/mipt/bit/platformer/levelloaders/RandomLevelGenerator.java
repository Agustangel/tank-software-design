package ru.mipt.bit.platformer.levelloaders;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.GridPoint2;
import org.springframework.stereotype.Component;

import ru.mipt.bit.platformer.app.GameObjectParams;
import ru.mipt.bit.platformer.controller.LevelController;
import ru.mipt.bit.platformer.controller.LevelControllerFactory;
import ru.mipt.bit.platformer.controller.ObstacleController;
import ru.mipt.bit.platformer.controller.TankController;
import ru.mipt.bit.platformer.model.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Генератор случайных уровней
 */
@Component
public class RandomLevelGenerator implements LevelGenerator {
    
    private static final int LEVEL_WIDTH = 20;
    private static final int LEVEL_HEIGHT = 16;
    private static final double OBSTACLE_DENSITY = 0.25;
    
    @Override
    public LevelController generateLevel(TiledMap tiledMap, Batch batch,
                                        LevelControllerFactory factory) {
        Level levelModel = new Level(LEVEL_WIDTH, LEVEL_HEIGHT,
                                              new GridPoint2(1, 1));
        
        // Генерировать препятствия
        generateObstacles(levelModel);
        
        // Создать LevelController
        return factory.createLevelController(tiledMap, levelModel, batch);
    }
    
    private void generateObstacles(Level levelModel) {
        Random random = new Random();
        
        for (int x = 0; x < LEVEL_WIDTH; x++) {
            for (int y = 0; y < LEVEL_HEIGHT; y++) {
                if (random.nextDouble() < OBSTACLE_DENSITY) {
                    levelModel.addObstacle(new GridPoint2(x, y));
                }
            }
        }
    }
    
    @Override
    public String getName() {
        return "RandomLevelGenerator";
    }
}
