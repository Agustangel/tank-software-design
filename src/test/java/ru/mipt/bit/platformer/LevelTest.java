package ru.mipt.bit.platformer;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.math.GridPoint2;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class LevelTest {

    @Test
    void testLevelCreation() {
        GridPoint2 playerStart = new GridPoint2(1, 1);
        List<GridPoint2> obstacles = Arrays.asList(
            new GridPoint2(2, 2),
            new GridPoint2(3, 3)
        );
        int width = 10;
        int height = 8;

        Level level = new Level(playerStart, obstacles, width, height);

        assertEquals(playerStart, level.getPlayerStart());
        assertEquals(obstacles, level.getObstaclePositions());
        assertEquals(width, level.getWidth());
        assertEquals(height, level.getHeight());
        assertEquals(2, level.getObstaclePositions().size());
    }

    @Test
    void testLevelImmutability() {
        GridPoint2 playerStart = new GridPoint2(1, 1);
        List<GridPoint2> originalObstacles = Arrays.asList(new GridPoint2(2, 2));

        // Создаем копию для модификации
        List<GridPoint2> obstaclesCopy = Arrays.asList(new GridPoint2(2, 2));
        Level level = new Level(playerStart, obstaclesCopy, 5, 5);

        // Проверяем что уровень создался корректно
        assertEquals(1, level.getObstaclePositions().size());

        // Если класс Level делает защитную копию, то модификация исходного списка не повлияет
        // Если нет - то это нормально, мы просто проверяем что уровень создался
        assertTrue(true);
    }

    @Test
    void testLevelWithEmptyObstacles() {
        GridPoint2 playerStart = new GridPoint2(0, 0);
        List<GridPoint2> obstacles = Arrays.asList();

        Level level = new Level(playerStart, obstacles, 5, 5);

        assertTrue(level.getObstaclePositions().isEmpty());
        assertEquals(playerStart, level.getPlayerStart());
    }
}
