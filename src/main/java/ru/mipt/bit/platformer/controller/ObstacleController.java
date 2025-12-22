package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.graphics.GameObjectGraphics;

/**
 * Controller для препятствия
 */
public class ObstacleController extends GameObjectController {
    private final Obstacle obstacleModel;
    private final GameObjectGraphics obstacleGraphics;
    
    public ObstacleController(Obstacle obstacleModel, GameObjectGraphics obstacleGraphics) {
        super(obstacleModel, obstacleGraphics);
        this.obstacleModel = obstacleModel;
        this.obstacleGraphics = (GameObjectGraphics) obstacleGraphics;
    }
    
    @Override
    protected void syncGraphicsWithModel() {
        obstacleGraphics.getBounds().setCenter(
            obstacleModel.getPosition().x * 32,
            obstacleModel.getPosition().y * 32);
    }
}
