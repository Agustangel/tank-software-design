package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import java.util.List;

/**
 * Данные уровня - позиции объектов и размеры
 */
public class LevelData {
    private final GridPoint2 playerStart;
    private final List<GridPoint2> obstaclePositions;
    private final int width;
    private final int height;

    public LevelData(GridPoint2 playerStart, List<GridPoint2> obstaclePositions, int width, int height) {
        this.playerStart = playerStart;
        this.obstaclePositions = obstaclePositions;
        this.width = width;
        this.height = height;
    }

    public GridPoint2 getPlayerStart() { return playerStart; }
    public List<GridPoint2> getObstaclePositions() { return obstaclePositions; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}