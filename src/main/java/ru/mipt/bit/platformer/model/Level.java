package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.api.GameWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Model для уровня
 * Содержит: доступные позиции, препятствия, размер
 * Реализует GameWorld для выполнения проверок доступности
 */
public class Level implements GameWorld {
    private final int width;
    private final int height;
    private final GridPoint2 playerStart;
    private final List<GridPoint2> obstacles;
    
    public Level(int width, int height, GridPoint2 playerStart) {
        this.width = width;
        this.height = height;
        this.playerStart = new GridPoint2(playerStart);
        this.obstacles = new ArrayList<>();
    }
    
    public void addObstacle(GridPoint2 position) {
        obstacles.add(new GridPoint2(position));
    }
    
    public GridPoint2 getPlayerStart() { return playerStart; }
    public List<GridPoint2> getObstacles() { return new ArrayList<>(obstacles); }
    
    @Override
    public boolean isAvailableForMove(int x, int y) {
        // Проверка границ
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }
        
        // Проверка препятствий
        for (GridPoint2 obstacle : obstacles) {
            if (obstacle.x == x && obstacle.y == y) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public int getLevelWidth() { return width; }
    
    @Override
    public int getLevelHeight() { return height; }
}
